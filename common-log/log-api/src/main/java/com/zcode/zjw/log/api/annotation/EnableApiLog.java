package com.zcode.zjw.log.api.annotation;

import com.zcode.zjw.log.api.enhancer.ApiLogAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启参数日志打印
 * @author zhangjiwei
 */
@Import(ApiLogAspect.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableApiLog {
}