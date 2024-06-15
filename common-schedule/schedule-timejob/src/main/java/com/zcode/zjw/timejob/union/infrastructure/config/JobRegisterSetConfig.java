package com.zcode.zjw.timejob.union.infrastructure.config;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.zcode.zjw.timejob.union.infrastructure.factory.EventLoader;
import com.zcode.zjw.timejob.union.listener.event.BaseJobOperateEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledFuture;

/**
 * @author zhangjiwei
 * @description 任务事件注册集合配置
 * @date 2022/11/5 上午8:39
 */
@Configuration
public class JobRegisterSetConfig {

    /**
     * 加载查询服务
     */
    @Resource
    private EventLoader eventLoader;

    @Bean(name = "jobEventRegisterSet")
    public ConcurrentLinkedQueue<BaseJobOperateEvent> getJobEventRegisterSet() {
        ConcurrentLinkedQueue<BaseJobOperateEvent> taskEventRegisterSet = new ConcurrentLinkedQueue<>();
        eventLoader.getJobOperateMap().forEach((key, value) -> taskEventRegisterSet.add(value));
        return taskEventRegisterSet;
    }

    @Bean(name = "jobFutureMap")
    public ConcurrentHashMap<String, ConcurrentLinkedQueue<ScheduledFuture<?>>> getJobFutureSet() {
        return new ConcurrentHashMap<>();
    }

    @Bean(name = "workTaskThreadSet")
    public ConcurrentHashSet<Thread> geTaskWorkThreadSet() {
        return new ConcurrentHashSet<>();
    }

}
