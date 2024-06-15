package com.zcode.zjw.datasource.multiple.annotation;

import com.zcode.zjw.datasource.multiple.common.Const;

import java.lang.annotation.*;


/**
 * 数据源注解
 *
 * @author：zjw
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

    String value() default Const.DEFAULT;

}