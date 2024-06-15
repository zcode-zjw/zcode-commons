package com.zcode.zjw.timejob.union.infrastructure.config;

import cn.hutool.core.collection.ConcurrentHashSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangjiwei
 * @description 定时任务全局开始注册集合配置
 * @date 2022/12/11 下午8:51
 */
@Configuration
public class JobStartRunTableConfig {

    @Bean(name = "jobStartRunSet")
    public ConcurrentHashSet<String> initJobTaskLock() {
        return new ConcurrentHashSet<>();
    }

}
