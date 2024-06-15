package com.zcode.zjw.common.file.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: zjw
 * @date: 2022/12/25
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
public @interface Xml {

    /**
     * 注解是否是节点属性，默认为false，没有默认值添加注解时必填
     *
     * @return
     */
    boolean isAttr() default false;

    /**
     * 标志bean属性是否是xml元素,默认为false，是xml元素的属性需要设置为true
     *
     * @return
     */
    boolean isElement() default false;

    /**
     * 属性是否是list，默认为false
     *
     * @return
     */
    boolean isList() default false;

    /**
     * 属性bean的类型或list中bean的类型
     *
     * @return
     */
    Class clazz() default Object.class;

    /**
     * 属性对应的xml节点名称，一样不需要设置
     *
     * @return
     */
    String xmlName() default "";

    /**
     * 配置在Level注解中时必须配置bean的属性
     *
     * @return
     */
    String attrName() default "";

    /**
     * xml转date类型的格式
     *
     * @return
     */
    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

    int id() default -1;

}
