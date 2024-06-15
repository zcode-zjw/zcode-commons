package com.zcode.zjw.web.common;

import lombok.Getter;

/**
 * 业务异常
 * biz是business的缩写
 *
 * @author zhangjiwei
 * @see ExceptionCodeEnum
 */
@Getter
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 2886389526330036740L;

    private ExceptionCodeEnum error;

    /**
     * 构造器，有时我们需要将第三方异常转为自定义异常抛出，但又不想丢失原来的异常信息，此时可以传入cause
     *
     * @param error
     * @param cause
     */
    public BizException(ExceptionCodeEnum error, Throwable cause) {
        super(cause);
        this.error = error;
    }

    /**
     * 构造器，只传入错误枚举
     *
     * @param error
     */
    public BizException(ExceptionCodeEnum error) {
        this.error = error;
    }
}