package com.zcode.zjw.timejob.union.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author zhangjiwei
 * @description 定时任务配置文件对象
 * @date 2022/11/14 下午8:09
 */
@Configuration
@ConfigurationProperties(prefix = "timetask", ignoreUnknownFields = false)
//@PropertySource("classpath:oamp.properties")
@PropertySource("file:./config/oamp-timetask.properties")
@Data
@Component("timeTaskProperties")
public class TimeJobProperties {

    /**
     * 调度池大小
     */
    private Integer schedulerPoolSize;

    /**
     * 核心任务池大小
     */
    private Integer taskCorePoolSize;

    /**
     * 最大任务池大小
     */
    private Integer taskMaxPoolSize;

    /**
     * 任务池任务存活时间
     */
    private Integer taskKeepAliveTime;

    /**
     * 任务队列大小
     */
    private Integer taskQueueSize;

}