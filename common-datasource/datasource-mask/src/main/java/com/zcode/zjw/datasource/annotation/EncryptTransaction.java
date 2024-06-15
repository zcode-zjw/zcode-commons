package com.zcode.zjw.datasource.annotation;

import java.lang.annotation.*;

/**
 * 该注解有两种使用方式
 * ①：配合@SensitiveData加在类中的字段上
 * ②：直接在Mapper中的方法参数上使用
 **/
@Documented
@Inherited
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptTransaction {

}