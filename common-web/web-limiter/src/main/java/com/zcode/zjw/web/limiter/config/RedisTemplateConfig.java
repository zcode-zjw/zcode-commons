package com.zcode.zjw.web.limiter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * @author zhangjiwei
 * @since 2021/03/19 23:37
 **/
public class RedisTemplateConfig {
    /**
     * 向容器注入LimitRedisTemplate
     *
     * @param factory
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(LimitRedisTemplate.class)
    public LimitRedisTemplate limitRedisTemplate(RedisConnectionFactory factory) {
        LimitRedisTemplate template = new LimitRedisTemplate();
        template.setConnectionFactory(factory);
        return template;
    }
}
