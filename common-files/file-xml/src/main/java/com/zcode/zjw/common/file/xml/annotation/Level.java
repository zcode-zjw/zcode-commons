package com.zcode.zjw.common.file.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: zjw
 * @since: 2022/12/25
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface Level {

    /**
     * 类对应节点所在的层级
     *
     * @return
     */
    int value() default -1;

    /**
     * 类对应的节点名,不设置默认为类名
     *
     * @return
     */
    String xmlName() default "";

    Xml[] xmls() default {@Xml()};

    /**
     * 标志当前节点映射的bean就是一个list
     *
     * @return
     */
    boolean isList() default false;
}
