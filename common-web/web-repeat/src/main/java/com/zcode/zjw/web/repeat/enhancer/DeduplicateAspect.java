package com.zcode.zjw.web.repeat.enhancer;

import com.zcode.zjw.web.repeat.annotation.Deduplicate;
import com.zcode.zjw.web.repeat.common.DeduplicateMode;
import com.zcode.zjw.web.repeat.service.CheckDuplicateService;
import com.zcode.zjw.web.repeat.service.CheckDuplicateServiceLoader;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author zhangjiwei
 * @description 幂等放重复切面
 * @date 2023/2/8 上午10:43
 */
@Aspect
@Slf4j
public class DeduplicateAspect {

    @Autowired
    private CheckDuplicateServiceLoader checkDuplicateServiceLoader;

    @Pointcut("@annotation(com.zcode.zjw.web.repeat.annotation.Deduplicate) " +
            "|| @within(com.zcode.zjw.web.repeat.annotation.Deduplicate)")
    public void point() {
    }

    @Around("point()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        /* 判断使用哪种防重模式 */
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Deduplicate annotation = method.getAnnotation(Deduplicate.class);
        DeduplicateMode mode = annotation.mode();

        String id = request.getParameter("id");

        for (CheckDuplicateService checkDuplicateService : checkDuplicateServiceLoader.getCheckDuplicateServiceList()) {
            if (checkDuplicateService.supports(mode)) {
                // 如果信息表ID已经存在则不是新加信息，而是修改信息，放行
                if (!(id != null && !"".equals(id))) {
                    if (checkDuplicateService.repeatDataValidate(request, response)) {
                        System.out.println("重复的请求");
                        throw new Exception("重复的请求" + request.getRequestURL());
                    }
                }
                break;
            }
        }

        // 正常执行
        result = joinPoint.proceed();
        System.out.println("正常执行");
        return result;

    }

}

