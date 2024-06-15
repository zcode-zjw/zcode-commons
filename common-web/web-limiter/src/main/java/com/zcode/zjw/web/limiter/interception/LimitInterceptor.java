package com.zcode.zjw.web.limiter.interception;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zcode.zjw.common.utils.collection.ColUtils;
import com.zcode.zjw.common.utils.web.IpUtil;
import com.zcode.zjw.common.utils.web.ServletUtil;
import com.zcode.zjw.configs.common.Result;
import com.zcode.zjw.web.limiter.annotation.FlowRateLimiter;
import com.zcode.zjw.web.limiter.common.LimitType;
import com.zcode.zjw.web.limiter.config.LimitRedisTemplate;
import com.zcode.zjw.web.limiter.config.RedisScriptConfig;
import com.zcode.zjw.web.limiter.config.RedisTemplateConfig;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 接口限流拦截器
 *
 * @author zhangjiwei
 * @since 2021/03/19 10:
 **/
@Import({RedisScriptConfig.class, RedisTemplateConfig.class})
@Slf4j
public class LimitInterceptor implements HandlerInterceptor {

    private LimitRedisTemplate redisTemplate = SpringUtil.getBean(LimitRedisTemplate.class);

    @Autowired
    private RedisScript<Boolean> redisScript;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${zcode.web.flow-limiter.tip:访问过于频繁，请稍候再试}")
    private String flowLimiterTip;

    @Value("${zcode.web.flow-limiter.base-package-path:null}")
    private String basePackageScanPath;

    /**
     * 限流统计时间区间允许请求的次数
     */
    @Value("${zcode.web.flow-limiter.threshold:0}")
    private int threshold;

    /**
     * 限流统计时间区间，单位秒
     */
    @Value("${zcode.web.flow-limiter.ttl:0}")
    private int ttl;

    @Value("${zcode.web.flow-limiter.limit-key:null}")
    private String limitKey;

    @Value("${zcode.web.flow-limiter.limit-type:null}")
    private String limitType;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        try {
            if (handler instanceof HandlerMethod) {
                /* 把handler强转为HandlerMethod */
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                // 注解是否加在类上
                FlowRateLimiter flowRateLimiterForClass = handlerMethod.getBeanType().getAnnotation(FlowRateLimiter.class);
                /* 从handlerMethod中获取本次请求的接口方法对象然后判断该方法上是否标有我们自定义的注解@RateLimiter */
                FlowRateLimiter flowRateLimiterForMethod = handlerMethod.getMethod().getAnnotation(FlowRateLimiter.class);
                if (flowRateLimiterForMethod != null || flowRateLimiterForClass != null) {
                    return limitDeal(Optional.ofNullable(flowRateLimiterForMethod).orElse(flowRateLimiterForClass), handlerMethod, response);
                }

                /* 如果配置了全局或扫描路径 */
                basePackageScanPath = basePackageScanPath.trim();
                if (!"null".equals(basePackageScanPath)) {
                    // 1、对于根目录就设置为*的
                    if ("*".equals(basePackageScanPath)) {
                        return limitDeal(FlowRateLimiter.class.newInstance(), handlerMethod, response);
                    }
                    // 2、对于设置了扫描路径的情况（不包含有通配符的情况）
                    if (basePackageScanPath.contains("*")) {
                        throw new Exception("扫描路径错误，暂不支持通配符 '*'");
                    }
                    String currentMethodPackagePath = handlerMethod.getBeanType().getPackage().getName();
                    if (scanPathContains(currentMethodPackagePath, basePackageScanPath)) {
                        FlowRateLimiter templateAnnotation = LimiterTemplate.class.getDeclaredMethod("templateMethod").getAnnotation(FlowRateLimiter.class);
                        return limitDeal(templateAnnotation, handlerMethod, response);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }

        return true;
    }

    /**
     * 类包路径是否包含于扫描包路径
     *
     * @return 是/否
     */
    private boolean scanPathContains(String currentClassPackagePath, String basePackageScanPath) {
        // 如果当前类的包路径都不在扫描包路径，直接返回false，如果当前类的包路径长度小于扫描包路径，返回false
        if (basePackageScanPath.contains(currentClassPackagePath)
                || basePackageScanPath.length() > currentClassPackagePath.length()) {
            return false;
        }
        // 按.分割开，从第一个开始比较
        String[] basePackageScanPathArray = basePackageScanPath.split("\\.");
        String[] currentClassPackagePathArray = currentClassPackagePath.split("\\.");
        int iterNum = Math.min(basePackageScanPathArray.length, currentClassPackagePathArray.length);
        for (int i = 0; i < iterNum; i++) {
            // 如果对应位置的值不同，返回false
            if (!basePackageScanPathArray[i].equals(currentClassPackagePathArray[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 限流注解模版类
     */
    static class LimiterTemplate {
        @FlowRateLimiter
        public void templateMethod() {

        }
    }


    /**
     * 限流处理逻辑
     *
     * @param flowRateLimiter 当前方法上注解对象
     * @param handlerMethod   方法
     * @return 是否被限流
     */
    private boolean limitDeal(FlowRateLimiter flowRateLimiter, HandlerMethod handlerMethod,
                              HttpServletResponse response) throws JsonProcessingException {
        String initLimitKey = flowRateLimiter.key();
        LimitType initLimitType = flowRateLimiter.limitType();
        int initTtl = flowRateLimiter.ttl();
        int initThreshold = flowRateLimiter.threshold();
        if (!"null".equals(limitKey)) {
            initLimitKey = limitKey;
        }
        if (!"null".equals(limitType)) {
            initLimitType = LimitType.selectLimitType(limitType);
        }
        if (ttl != 0) {
            initTtl = ttl;
        }
        if (threshold != 0) {
            initThreshold = threshold;
        }
        List<String> keys = ColUtils.toCol(getCombineKey(handlerMethod.getMethod(), initLimitKey, initLimitType));
        List<String> args = Arrays.asList(String.valueOf(initThreshold), String.valueOf(initTtl));
        Boolean result = redisTemplate.execute(redisScript, keys, args.toArray());
        /* 服务被限流 */
        if (result != null && result) {
            String msg = String.format(flowLimiterTip + "，当前请求允许访问速率为{%d次/%d秒}", initThreshold, initTtl);
            log.warn(msg);
            Result<?> res = Result.error(msg);
            ServletUtil.renderString(response, objectMapper.writeValueAsString(res));
            return false;
        }
        return true;
    }

    private String getCombineKey(Method method, String initLimitKey, LimitType initLimitType) {
        HttpServletRequest request = ServletUtil.getRequest();
        StringBuilder sb = new StringBuilder(initLimitKey);
        if (initLimitType == LimitType.IP) {
            sb.append(IpUtil.getIpAddr(request)).append("-");
        } else if (initLimitType == LimitType.USER) {
            String userId = request.getHeader("userid");
            if (userId != null) {
                sb.append(userId).append("-");
            } else {
                sb.append(request.getSession().getId()).append("-");
            }
        }
        Class<?> targetClass = method.getDeclaringClass();
        sb.append(targetClass.getName()).append("-").append(method.getName());
        return sb.toString();
    }
}
