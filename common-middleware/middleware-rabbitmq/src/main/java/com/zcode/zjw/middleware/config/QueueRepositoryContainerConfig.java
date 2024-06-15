package com.zcode.zjw.middleware.config;

import com.zcode.zjw.middleware.manager.MqTypeEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author zhangjiwei
 * @description 队列仓库
 * @date 2023/2/14 下午2:01
 */
@Configuration
public class QueueRepositoryContainerConfig {

    @Bean(name = "queueRepositoryContainer")
    public ConcurrentHashMap<MqTypeEnum, CopyOnWriteArrayList<ConcurrentHashMap<String, Object>>> initQueueRepositoryContainer() {
        ConcurrentHashMap<MqTypeEnum, CopyOnWriteArrayList<ConcurrentHashMap<String, Object>>> queueRepositoryContainer = new ConcurrentHashMap<>();
        // 初始化所有Mq类型，初始状态为线程安全的空列表
        for (MqTypeEnum value : MqTypeEnum.values()) {
            queueRepositoryContainer.put(value, new CopyOnWriteArrayList<>());
        }
        return queueRepositoryContainer;
    }

}
