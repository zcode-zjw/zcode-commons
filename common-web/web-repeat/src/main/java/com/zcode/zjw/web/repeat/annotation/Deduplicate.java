package com.zcode.zjw.web.repeat.annotation;

import com.zcode.zjw.web.repeat.common.DeduplicateMode;

import java.lang.annotation.*;

/**
 * @author zhangjiwei
 * @description 请求去重
 * @date 2023/2/8 上午10:40
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Deduplicate {

    int count() default 1;

    DeduplicateMode mode() default DeduplicateMode.SESSION;

}
