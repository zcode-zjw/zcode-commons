package com.zcode.zjw.timejob.union.infrastructure.annotation;

import org.springframework.stereotype.Component;

/**
 * @author zhangjiwei
 * @description 增强者
 * @date 2022/11/13 上午11:56
 */
@Component
public @interface Enhancer {

    String name() default "";

}
