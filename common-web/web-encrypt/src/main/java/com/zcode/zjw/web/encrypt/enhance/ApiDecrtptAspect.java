package com.zcode.zjw.web.encrypt.enhance;

import com.zcode.zjw.web.encrypt.annotation.ApiDecrypt;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author zhangjiwei
 * @description 接口解密切面类
 * @date 2023/2/8 上午10:43
 */
@Aspect
@Slf4j
public class ApiDecrtptAspect {

    @Pointcut("@annotation(com.zcode.zjw.web.encrypt.annotation.ApiDecrypt) " +
            "|| @within(com.zcode.zjw.web.encrypt.annotation.ApiDecrypt)")
    public void point() {
    }

    @Around("point()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        /* 判断使用哪种防重模式 */
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        ApiDecrypt annotation = method.getAnnotation(ApiDecrypt.class);

        return result;

    }

}

