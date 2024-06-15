package com.zcode.zjw.timejob.union.infrastructure.annotation;

import org.springframework.stereotype.Component;

/**
 * @author zhangjiwei
 * @description 加载者
 * @date 2022/11/5 下午12:02
 */
@Component
public @interface Loader {
    String name() default "";
}
