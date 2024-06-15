package com.zcode.zjw.timejob.union.infrastructure.annotation;

import org.springframework.stereotype.Component;

/**
 * @author zhangjiwei
 * @description
 * @date 2022/11/5 上午11:15
 */
@Component
public @interface JobListener {

    String name() default "";

}
