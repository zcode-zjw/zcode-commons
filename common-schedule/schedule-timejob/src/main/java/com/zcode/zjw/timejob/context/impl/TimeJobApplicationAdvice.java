package com.zcode.zjw.timejob.context.impl;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.zcode.zjw.timejob.common.JobOperateTypeEnum;
import com.zcode.zjw.timejob.common.JobParamsEntity;
import com.zcode.zjw.timejob.context.AbstractTimeJobApplication;
import com.zcode.zjw.timejob.context.ITimeJobApplicationAdvice;
import com.zcode.zjw.timejob.union.listener.event.TimeJobEvent;
import com.zcode.zjw.timejob.union.listener.event.impl.TimeJobPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhangjiwei
 * @description 定时任务上下文
 * @date 2022/11/5 下午3:10
 */
@Component
@Slf4j
public class TimeJobApplicationAdvice extends AbstractTimeJobApplication implements ITimeJobApplicationAdvice {

    @Override
    public void restore() {
        // 发布恢复Job任务
        publishStoreJob();
    }

    /**
     * 发布的定时任务（包括:
     * 1、上一次未完成的任务（只运行一次）
     * 2、正常的定时任务
     */
    public void publishStoreJob() {
    }


    public void startJob(JobParamsEntity jobParamsEntity) {
        jobParamsEntity.setTaskOperateType(JobOperateTypeEnum.START);
        // 获取开始事件
        TimeJobEvent timeJobEvent = new TimeJobEvent(jobParamsEntity);
        timeJobEvent.setJobParamsEntity(jobParamsEntity);
        timeJobPublisher.publish(timeJobEvent);

    }


    @Override
    public ThreadPoolTaskScheduler getJobScheduler() {
        return threadPoolTimeTaskScheduler;
    }

    @Override
    public ThreadPoolExecutor getTaskExecutor() {
        return threadPoolExecutor;
    }

    @Override
    public ConcurrentHashMap<String, ConcurrentLinkedQueue<ScheduledFuture<?>>> getJobFutureMap() {
        return jobFutureMap;
    }

    @Override
    public ConcurrentHashSet<Thread> getWorkTaskLivedExecutor() {
        return workTaskThreadSet;
    }

    @Override
    public ReentrantLock getMutexLock() {
        return mutexLock;
    }

    @Override
    public ReentrantLock getOptimisticLock() {
        return null;
    }

    @Override
    public TimeJobPublisher getTimeTaskPublisher() {
        return timeJobPublisher;
    }

    @Override
    public JobParamsEntity.Builder buildJobParamsEntity() {
        return new JobParamsEntity.Builder();
    }

    @Override
    public void safeClose() throws InterruptedException {
        closeApplication(o -> threadPoolExecutor.shutdown());

    }

    @Override
    public void strongClose() throws InterruptedException {
        closeApplication(o -> threadPoolExecutor.shutdownNow());
    }

    @Override
    public void open() {
        log.info("正在开启任务调度池...");
        this.threadPoolTimeTaskScheduler.shutdown();
        this.threadPoolTimeTaskScheduler.initialize();
        log.info("开启任务调度池成功！");
    }

}
