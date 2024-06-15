package com.zcode.zjw.log.user.enhancer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zcode.zjw.cache.redis.service.RedisQueueService;
import com.zcode.zjw.cache.redis.utils.RedisCache;
import com.zcode.zjw.common.utils.common.ThreadLocalUtil;
import com.zcode.zjw.log.user.annotation.UserLog;
import com.zcode.zjw.log.user.common.MessageQueueEnum;
import com.zcode.zjw.log.user.entity.GhdqsOampUser;
import com.zcode.zjw.log.user.entity.LoginUser;
import com.zcode.zjw.log.user.pojo.GhdqsOampUserDTO;
import com.zcode.zjw.log.user.pojo.UserLogDTO;
import com.zcode.zjw.log.user.service.GhdqsOampUserLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 操作日志注解切面实现
 *
 * @author zhangjiwei
 */
@Slf4j
@Aspect
@Import(RedisCache.class)
public class UserLogAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GhdqsOampUserLogService ghdqsOampUserLogService;

    @Autowired
    private RedisQueueService redisQueueService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisCache redisCache;

    @Pointcut("@annotation(com.zcode.zjw.log.user.annotation.UserLog)")
    public void pointcut() {
    }

    @AfterReturning("pointcut()")
    public void afterReturning(JoinPoint point) {
        saveSysUserLog(point);
    }

    private void saveSysUserLog(JoinPoint point) {
        // 获取当前登录用户
        GhdqsOampUser userInfoDTO = getUserInfoDTO();

        // 目标方法、以及方法上的@UserLog注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        UserLog userLogAnnotation = method.getAnnotation(UserLog.class);
        if (userLogAnnotation == null) {
            return;
        }

        // 保存消息与日志
        saveMsgLog(point, userLogAnnotation, userInfoDTO);
    }

    private void saveMsgLog(JoinPoint point, UserLog userLogAnnotation, GhdqsOampUser userInfoDTO) {
        try {
            // 收集相关信息并保存
            UserLogDTO userLogDTO = new UserLogDTO();
            Object jsonObj = redisCache.getCacheObject("msg_id");
            if (jsonObj == null) {
                redisCache.setCacheObject("msg_id", 1);
                jsonObj = "1";
            }
            userLogDTO.setId(Long.parseLong(jsonObj + ""));
            redisTemplate.getConnectionFactory().getConnection().incr(redisTemplate.getKeySerializer().serialize("msg_id"));
            userLogDTO.setModuleCode(userLogAnnotation.module().getModuleCode());
            userLogDTO.setContent(getContentJson(point));
            userLogDTO.setTitle(userLogAnnotation.title());
            userLogDTO.setOperatorId(userInfoDTO.getId());
            userLogDTO.setOperateTime(new Date());
            userLogDTO.setType(userLogAnnotation.type().getValue());

            // 存入数据库
            ghdqsOampUserLogService.addSysLog(userLogDTO);
            // 存入消息队列
            try {
                GhdqsOampUserDTO arg = (GhdqsOampUserDTO) point.getArgs()[0];
                userLogDTO.setUsername(Optional.ofNullable(arg.getUserName()).orElse(userInfoDTO.getName()));
            } catch (ClassCastException e) {
                userLogDTO.setUsername(Optional.ofNullable(userInfoDTO.getName()).orElse(null));
            }
            redisQueueService.pushQueue(MessageQueueEnum.ORDER_MESSAGE.getMsgFlag(), userLogDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private GhdqsOampUser getUserInfoDTO() {
        AtomicReference<GhdqsOampUser> userInfoDTO = new AtomicReference<>(new GhdqsOampUser());
        Optional.ofNullable(ThreadLocalUtil.get("currentUser")).map(param -> {
            LoginUser loginUser = (LoginUser) param;
            userInfoDTO.set(loginUser.getUser());
            return userInfoDTO;
        });
        return userInfoDTO.get();
    }

    private String getContentJson(JoinPoint point) {
        String requestType = request.getMethod();
        if ("GET".equals(requestType)) {
            // 如果是GET请求，直接返回QueryString（目前没有针对查询操作进行日志记录，先留着吧）
            return request.getQueryString();
        }

        Object[] args = point.getArgs();
        Object[] arguments = new Object[args.length];

        for (int i = 0; i < args.length; i++) {
            // 只打印客户端传递的参数，排除Spring注入的参数，比如HttpServletRequest
            if (args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }

        try {
            return objectMapper.writeValueAsString(arguments);
        } catch (JsonProcessingException e) {
            log.error("UserLogAspect#getContentJson JsonProcessingException", e);
        }
        return "";
    }
}