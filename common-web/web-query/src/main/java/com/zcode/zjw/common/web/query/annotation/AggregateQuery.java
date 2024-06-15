package com.zcode.zjw.common.web.query.annotation;

import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

/**
 * 聚合查询
 *
 * @author zhangjiwei
 * @since 2023/8/11
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ResponseBody
@Documented
public @interface AggregateQuery {

    Class<?> vo();

    String mapper() default "";

    Class<?> mapperClass() default Class.class;

    String mapperMethod() default "";

    String mapperArgsKey() default "";

    Class<?> mapperArgsType() default Class.class;

}
