package com.zcode.zjw.timejob.union.infrastructure.annotation;

import org.springframework.stereotype.Component;

/**
 * @author zhangjiwei
 * @description 事件工厂注解
 * @date 2022/11/5 上午11:09
 */
@Component
public @interface EventFactory {

    String name() default "";

}
