package com.zcode.zjw.common.middleware.canal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * canal线程池配置
 *
 * @author zhangjiwei
 * @date 2023/6/9
 */
@Configuration
public class ZcodeCanalThreadPoolConfig {

    @Bean("zcodeCanalThreadPool")
    public ThreadPoolTaskExecutor zcodeCanalThreadPoolInit() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(1);
        threadPoolTaskExecutor.setMaxPoolSize(2);
        threadPoolTaskExecutor.setQueueCapacity(2);
        threadPoolTaskExecutor.setAwaitTerminationMillis(60000);
        threadPoolTaskExecutor.setKeepAliveSeconds(60000);
        return threadPoolTaskExecutor;
    }

}
