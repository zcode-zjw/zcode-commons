package com.zcode.zjw.log.timing.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author zhangjiwei
 * @description Websocket参数配置文件对象
 * @date 2022/11/14 下午8:09
 */
@Configuration
@PropertySource("classpath:zcode-time-log.properties")
@ConfigurationProperties(prefix = "websocket", ignoreUnknownFields = false)
//@PropertySource("file:./config/zcode-time-log.properties")
@Data
@Component("timeLogWebsocketProperties")
public class TimeLogWebsocketProperties {

    private String coreSize;

    private String maxSize;

    private String queueCapacity;

    private String keepAliveSeconds;

    private String awaitTerminationSeconds;

}