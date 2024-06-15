package com.zcode.zjw.web.limiter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @author zhangjiwei
 * @since 2021/03/17 21:45
 **/
public class RedisScriptConfig {

    @Bean
    public RedisScript<Boolean> redisScript() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        /* 从IO流中注入lua脚本 */
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("META-INF/lua/redis-limit.lua")));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }
}
