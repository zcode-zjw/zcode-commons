package com.zcode.zjw.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 角色权限注解
 * 注意：你首先要是一个用户才有权限，所以谈权限离不开登录，所以我在RequirePermission上加了LoginRequired
 */
@LoginRequired
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequiresPermission {

}