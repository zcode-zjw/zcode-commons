package com.zcode.zjw.web.limiter.annotation;


import com.zcode.zjw.web.limiter.common.LimitType;

import java.lang.annotation.*;

/**
 * 限流注解
 *
 * @author zhangjiwei
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FlowRateLimiter {

    /**
     * 限流 redis key
     */
    static final String RATE_LIMIT_KEY = "rate_limit::";

    /**
     * 限流统计时间区间，单位秒
     */
    int ttl() default 1;

    /**
     * 限流统计时间区间允许请求的次数
     */
    int threshold() default 10;

    /**
     * 限流key
     */
    String key() default RATE_LIMIT_KEY;

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.IP;
}
