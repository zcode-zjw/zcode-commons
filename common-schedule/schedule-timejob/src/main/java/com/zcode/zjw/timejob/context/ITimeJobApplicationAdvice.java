package com.zcode.zjw.timejob.context;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.zcode.zjw.timejob.common.JobParamsEntity;
import com.zcode.zjw.timejob.union.listener.event.impl.TimeJobPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhangjiwei
 * @description 定时任务上下文接口
 * @date 2022/11/5 下午3:09
 */
public interface ITimeJobApplicationAdvice {

    ThreadPoolTaskScheduler getJobScheduler();

    ThreadPoolExecutor getTaskExecutor();

    ConcurrentHashMap<String, ConcurrentLinkedQueue<ScheduledFuture<?>>> getJobFutureMap();

    ConcurrentHashSet<Thread> getWorkTaskLivedExecutor();

    ReentrantLock getMutexLock();

    ReentrantLock getOptimisticLock();

    TimeJobPublisher getTimeTaskPublisher();

    JobParamsEntity.Builder buildJobParamsEntity();

    /**
     * 安全关闭上下文
     *
     * @throws InterruptedException
     */
    void safeClose() throws InterruptedException;

    /**
     * 强制关闭上下文
     *
     * @throws InterruptedException
     */
    void strongClose() throws InterruptedException;

    /**
     * 开启上下文
     */
    void open();

    /**
     * 恢复上下文
     */
    void restore();

}
