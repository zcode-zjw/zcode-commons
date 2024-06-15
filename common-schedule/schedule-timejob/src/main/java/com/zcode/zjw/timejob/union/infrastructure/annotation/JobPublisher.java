package com.zcode.zjw.timejob.union.infrastructure.annotation;

import org.springframework.stereotype.Component;

/**
 * @author zhangjiwei
 * @description 任务发布者注解
 * @date 2022/11/5 上午11:24
 */
@Component
public @interface JobPublisher {

    String name() default "";

}
