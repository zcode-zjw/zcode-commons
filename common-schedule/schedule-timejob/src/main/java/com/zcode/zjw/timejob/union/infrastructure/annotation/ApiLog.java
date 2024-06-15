package com.zcode.zjw.timejob.union.infrastructure.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhangjiwei
 * @description 在需要记录接口请求信息的类或方法上添加
 * @date 2022/11/5 上午11:09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ApiLog {
}