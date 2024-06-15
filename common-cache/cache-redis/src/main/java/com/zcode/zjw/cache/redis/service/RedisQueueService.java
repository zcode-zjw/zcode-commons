package com.zcode.zjw.cache.redis.service;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangjiwei
 * @description Redis队列服务
 * @date 2022/11/19 上午9:52
 */
public interface RedisQueueService {

    String API_MONITOR_KEY = "api-monitor";

    String API_MONITOR_TMP_KEY = "api-monitor-tmp";

    /**
     * 向队列插入消息
     *
     * @param queue 自定义队列名称
     * @param obj   要存入的消息
     */
    void pushQueue(String queue, Object obj);

    /**
     * 从队列取出消息
     *
     * @param queue    自定义队列名称
     * @param timeout  最长阻塞等待时间
     * @param timeUnit 时间单位
     * @return
     */
    Object popQueue(String queue, long timeout, TimeUnit timeUnit);
}