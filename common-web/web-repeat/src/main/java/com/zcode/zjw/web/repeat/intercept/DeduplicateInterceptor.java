package com.zcode.zjw.web.repeat.intercept;

import com.zcode.zjw.cache.redis.utils.RedisCache;
import com.zcode.zjw.common.utils.common.JwtUtil;
import com.zcode.zjw.common.utils.common.ObjectUtil;
import com.zcode.zjw.common.utils.common.ThreadLocalUtil;
import com.zcode.zjw.security.entity.LoginUser;
import com.zcode.zjw.web.repeat.common.RepeatConstant;
import com.zcode.zjw.web.repeat.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 为请求携带上幂等请求Token
 *
 * @author zhangjiwei
 */
@Slf4j
@Order(9999)
public class DeduplicateInterceptor implements HandlerInterceptor {

    @Value("${zcode.repeat.open:false}")
    private Boolean open;

    @Autowired
    private RedisCache redisCache;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                             @NotNull Object handler) {
        try {
            if (open != null && open && handler instanceof HandlerMethod) {
                LoginUser loginUser = (LoginUser) ThreadLocalUtil.get("currentUser");
                if (loginUser != null && loginUser.getUser() != null && !ObjectUtil.isAllFieldNull(loginUser.getUser())) {
                    String token = JwtUtil.createJWT(loginUser.getUser().getId() + "", RepeatConstant.tokenSecretText);
                    // 如果已经有了，重新设置
                    if (response.getHeader(RepeatConstant.tokenFlag) != null) {
                        response.setHeader(RepeatConstant.tokenFlag, token);
                    } else {
                        response.addHeader(RepeatConstant.tokenFlag, token);
                    }
                    // 将请求信息存入Redis，设置超时时间为1小时
                    redisCache.setCacheObject(getNowUrlParams(request), true, 1, TimeUnit.HOURS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    String getNowUrlParams(HttpServletRequest httpServletRequest) {
        //String params=IdGen.generateShortUuid();
        //获取全部请求内容
        String params = JsonMapper.obj2String(httpServletRequest.getParameterMap());

        String url = httpServletRequest.getRequestURI();
        Map<String, String> map = new HashMap<>();
        map.put(url, params);
        return map.toString();
    }

}  
 