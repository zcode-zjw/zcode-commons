package com.zcode.zjw.timejob.union.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.annotation.Resource;

/**
 * @author zhangjiwei
 * @description Job任务调度池配置
 * @date 2022/11/4 下午8:18
 */
@Configuration
public class ThreadJobSchedulerConfig {

    @Resource
    private TimeJobProperties timeJobProperties;

    @Bean(name = "threadPoolTimeTaskScheduler")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(timeJobProperties.getSchedulerPoolSize());
        threadPoolTaskScheduler.setBeanName("threadPoolTimeTaskScheduler");
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }

}
