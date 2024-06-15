package com.zcode.zjw.timejob.union.infrastructure.annotation;

import org.springframework.stereotype.Component;

/**
 * @author zhangjiwei
 * @description 任务执行者
 * @date 2022/11/6 上午9:32
 */
@Component
public @interface JobHandler {

    String name() default "";

}
