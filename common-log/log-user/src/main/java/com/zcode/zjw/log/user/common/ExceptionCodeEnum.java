package com.zcode.zjw.log.user.common;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 通用错误枚举
 *
 * @author zhangjiwei
 */
@Getter
public enum ExceptionCodeEnum {

    /**
     * 通用结果
     */
    ERROR(-1, "网络错误"),
    SUCCESS(200, "成功"),
    NEED_LOGIN(10001, "需要登录"),
    PERMISSION_DENY(10002, "权限不足"),
    ERROR_PARAM(10004, "参数错误"),
    PAY_ERROR(10006, "支付失败"),
    AUTH_ERROR(401, "认证失败"),
    TOKEN_EXPIRE(10005, "身份过期");

    private final Integer code;
    private final String desc;

    ExceptionCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<Integer, ExceptionCodeEnum> cache = new HashMap<>();

    static {
        for (ExceptionCodeEnum exceptionCodeEnum : ExceptionCodeEnum.values()) {
            cache.put(exceptionCodeEnum.code, exceptionCodeEnum);
        }
    }

    public static String getDesc(Integer code) {
        return Optional.ofNullable(cache.get(code))
                .map(ExceptionCodeEnum::getDesc)
                .orElseThrow(() -> new IllegalArgumentException("invalid exception code!"));
    }

}