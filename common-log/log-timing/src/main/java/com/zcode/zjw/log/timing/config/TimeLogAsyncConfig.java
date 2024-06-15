package com.zcode.zjw.log.timing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * @author zhangjiwei
 * @description 异步线程池配置
 * @date 2022/11/14 下午8:10
 */
@EnableAsync
@Configuration
public class TimeLogAsyncConfig {

    @Resource
    private TimeLogWebsocketProperties timeLogWebsocketProperties;

    @Bean("timgLogFileListenerExecutor")
    public Executor logFileListenerExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(validateConfigInfo(() -> {
            return timeLogWebsocketProperties.getCoreSize();
        }, 8));
        taskExecutor.setMaxPoolSize(validateConfigInfo(() -> {
            return timeLogWebsocketProperties.getMaxSize();
        }, 16));
        taskExecutor.setQueueCapacity(validateConfigInfo(() -> {
            return timeLogWebsocketProperties.getQueueCapacity();
        }, 20));
        taskExecutor.setKeepAliveSeconds(validateConfigInfo(() -> {
            return timeLogWebsocketProperties.getKeepAliveSeconds();
        }, 60));
        taskExecutor.setThreadNamePrefix("logFileListenerExecutor--");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(validateConfigInfo(() -> {
            return timeLogWebsocketProperties.getAwaitTerminationSeconds();
        }, 60));
        return taskExecutor;
    }

    /**
     * 校验配置信息
     *
     * @param
     * @param defaultValue
     * @return
     */
    private int validateConfigInfo(Supplier<String> supplier, int defaultValue) {
        try {
            return Integer.parseInt(Optional.ofNullable(supplier.get()).orElse(defaultValue + ""));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

}
