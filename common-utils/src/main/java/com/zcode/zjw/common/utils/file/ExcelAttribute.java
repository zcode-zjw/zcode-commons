package com.zcode.zjw.common.utils.file;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelAttribute {
    /**
     * 对应的列名称
     */
    String value() default "";

    /**
     * 列序号
     */
    int sort();

    // 字段类型对应的格式（大家自己可以试着扩展）
    String format() default "";
}