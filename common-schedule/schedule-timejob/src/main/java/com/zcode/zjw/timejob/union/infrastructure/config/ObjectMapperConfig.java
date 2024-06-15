package com.zcode.zjw.timejob.union.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangjiwei
 * @description 初始化ObjectMapper
 * @date 2022/11/13 下午12:04
 */
@Configuration
public class ObjectMapperConfig {

    @Bean("objectMapper")
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

}
