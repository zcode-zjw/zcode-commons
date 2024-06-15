package com.zcode.zjw.cache.redis.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zcode.zjw.cache.redis.service.RedisQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangjiwei
 * @description Redis队列服务
 * @date 2022/11/19 上午9:52
 */
@Component
public class RedisQueueServiceImpl implements RedisQueueService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void pushQueue(String queue, Object obj) {
        try {
            redisTemplate.opsForList().leftPush(queue, objectMapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object popQueue(String queue, long timeout, TimeUnit timeUnit) {
        return redisTemplate.opsForList().rightPop(queue, timeout, timeUnit);
    }
}