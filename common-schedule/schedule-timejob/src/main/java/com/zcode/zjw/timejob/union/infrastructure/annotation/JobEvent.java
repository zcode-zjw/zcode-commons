package com.zcode.zjw.timejob.union.infrastructure.annotation;

import org.springframework.stereotype.Component;

/**
 * @author zhangjiwei
 * @description
 * @date 2022/11/5 上午11:14
 */
@Component
public @interface JobEvent {

    String name() default "";

}
