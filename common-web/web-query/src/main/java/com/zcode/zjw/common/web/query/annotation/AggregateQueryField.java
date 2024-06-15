package com.zcode.zjw.common.web.query.annotation;

import java.lang.annotation.*;

/**
 * 聚合查询字段
 *
 * @author zhangjiwei
 * @since 2023/8/11
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AggregateQueryField {

    /**
     * 精确匹配：exact
     * 模糊匹配：fuzzy
     */
    AggregateQueryRegexMode regexMode() default AggregateQueryRegexMode.EXACT;

    boolean condition() default false;


}
