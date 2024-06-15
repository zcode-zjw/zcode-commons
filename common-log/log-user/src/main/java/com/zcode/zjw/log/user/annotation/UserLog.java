package com.zcode.zjw.log.user.annotation;


import com.zcode.zjw.log.user.common.ModuleEnum;
import com.zcode.zjw.log.user.common.OperationEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户操作日志注解
 * @author zhangjiwei
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UserLog {
	/**
	 * 所属模块名
	 */
	ModuleEnum module();

	/**
	 * 操作标题
	 */
	String title();

	/**
	 * 操作类型
	 */
	OperationEnum type();
}