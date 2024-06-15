package com.zcode.zjw.timejob.union.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangjiwei
 * @description 任务线程池配置
 * @date 2022/11/5 下午3:48
 */
@Configuration
public class JobThreadPoolConfig {

    @Resource
    private TimeJobProperties timeJobProperties;

    @Bean(name = "taskThreadPoolExecutor")
    public ThreadPoolExecutor getTaskThreadPoolExecutor() {
        return new ThreadPoolExecutor(
                timeJobProperties.getTaskCorePoolSize(),
                timeJobProperties.getTaskMaxPoolSize(),
                timeJobProperties.getTaskKeepAliveTime(),
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(timeJobProperties.getTaskQueueSize()),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
