package com.zcode.zjw.log.trace.domain.core.subject.monitor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zcode.zjw.common.utils.common.DateUtils;
import com.zcode.zjw.log.trace.domain.request.repository.mapper.ApiRequestRecordMapper;
import com.zcode.zjw.log.trace.domain.request.repository.po.ApiRequestRecord;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 接口请求切面
 *
 * @author zhangjiwei
 * @since 2023/8/9
 */
@Aspect
@Component
@Slf4j
public class ApiRequestAspect {

    private final ObjectMapper objectMapper;

    private final ApiRequestRecordMapper apiRequestRecordMapper;

    public ApiRequestAspect(ObjectMapper objectMapper, ApiRequestRecordMapper apiRequestRecordMapper) {
        this.objectMapper = objectMapper;
        this.apiRequestRecordMapper = apiRequestRecordMapper;
    }


    @Pointcut("execution(* com.zcode.zjw..controller..*(..)) || execution(* com.zcode.zjw..facade..*(..)) " +
            "|| (@within(com.zcode.zjw.log.trace.domain.core.subject.monitor.ApiRecord) || @annotation(com.zcode.zjw.log.trace.domain.core.subject.monitor.ApiRecord))")
    public void controllerPointcut() {
    }

    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (requestAttributes != null) {
            request = requestAttributes.getRequest();
        }

        Signature signature = proceedingJoinPoint.getSignature();
        String methodName = signature.getName();

        Class<?> controllerClazz = signature.getDeclaringType();
        String controllerName = controllerClazz.getName();
        String apiRequestId = UUID.randomUUID().toString();

        insertApiRequestRecord(proceedingJoinPoint, request, methodName, controllerName, apiRequestId);

        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
            updateApiRequestRecord(apiRequestId, startTime, result, "SUC");
        } catch (Throwable e) {
            updateApiRequestRecord(apiRequestId, startTime, result, "ERR");
            throw new RuntimeException(e);
        }

        return result;
    }

    private void insertApiRequestRecord(ProceedingJoinPoint proceedingJoinPoint, HttpServletRequest request, String methodName, String controllerName, String apiRequestId) {
        try {
            ApiRequestRecord apiRequestRecord = new ApiRequestRecord();
            apiRequestRecord.setId(apiRequestId);
            apiRequestRecord.setRequestDatetime(DateUtils.getCurrentDateStringFormat());
            apiRequestRecord.setParams(getParamJSon(request, proceedingJoinPoint));
            apiRequestRecord.setRemoteAddress(getClientIp(request));
            apiRequestRecord.setRecordClassMethodFlag(String.format("%s#%s", controllerName, methodName));
            apiRequestRecord.setUrl(request.getRequestURL().toString());
            apiRequestRecord.setMethod(request.getMethod());
            apiRequestRecord.setHeaders(objectMapper.writeValueAsString(getHeaders(request)));
            apiRequestRecordMapper.insert(apiRequestRecord);
        } catch (Exception e) {
            log.error("记录接口请求信息失败", e);
        }
    }

    private void updateApiRequestRecord(String apiRequestId, long startTime, Object result, String status) {
        try {
            ApiRequestRecord apiRequestRecord = new ApiRequestRecord();
            apiRequestRecord.setId(apiRequestId);
            apiRequestRecord.setCost(String.valueOf(System.currentTimeMillis() - startTime));
            apiRequestRecord.setResponseRes(objectMapper.writeValueAsString(result));
            apiRequestRecord.setStatus(status);
            apiRequestRecordMapper.updateById(apiRequestRecord);
        } catch (Exception ex) {
            log.error("修改接口请求信息失败", ex);
        }
    }

    // --------- private methods ----------

    private Map<String, Object> getHeaders(HttpServletRequest request) {
        Map<String, Object> headersMap = new HashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headersMap.put(headerName, headerValue);
        }

        return headersMap;
    }

    private String getParamJSon(HttpServletRequest request, JoinPoint joinPoint) throws JsonProcessingException {
        String requestType = request.getMethod();
        if ("GET".equals(requestType)) {
            // 如果是GET请求，直接返回QueryString
            Map<String, Object> getParams = new HashMap<>();
            String queryString = request.getQueryString();
            String[] queryStringArray = queryString.split("&");
            for (String queryStr : queryStringArray) {
                String[] split = queryStr.split("=");
                if (split.length > 1) {
                    getParams.put(split[0], split[1]);
                }
            }
            return objectMapper.writeValueAsString(getParams);
        }

        Object[] args = joinPoint.getArgs();
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

        return objectMapper.writeValueAsString(arguments);
    }


    private String getClientIp(HttpServletRequest request) {
        // 一般都会有代理转发，真实的ip会放在X-Forwarded-For
        String xff = request.getHeader("X-Forwarded-For");
        if (xff == null) {
            return request.getRemoteAddr();
        } else {
            return xff.contains(",") ? xff.split(",")[0] : xff;
        }
    }

}
