package com.zcode.zjw.common.web.query.annotation;

import java.lang.annotation.*;

/**
 * 查询解析
 *
 * @author zhangjiwei
 * @since 2023/8/11
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueryParser {

    String value();

}
