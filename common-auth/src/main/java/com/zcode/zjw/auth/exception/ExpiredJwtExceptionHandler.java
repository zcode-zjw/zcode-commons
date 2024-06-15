package com.zcode.zjw.auth.exception;

import com.zcode.zjw.web.common.Result;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * token过期异常处理
 *
 * @author zhangjiwei
 * @since 2023/7/22
 */
@RestControllerAdvice
@Slf4j
public class ExpiredJwtExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public Result<Object> tokenExpiredDeal(ExpiredJwtException e) {
        log.error("身份已过期，请重新登录！");
        return Result.error("身份已过期，请重新登录！");
    }

}
