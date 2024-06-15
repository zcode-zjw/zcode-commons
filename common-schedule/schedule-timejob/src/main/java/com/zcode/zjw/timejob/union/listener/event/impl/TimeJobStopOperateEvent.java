package com.zcode.zjw.timejob.union.listener.event.impl;

import com.zcode.zjw.timejob.common.IJobOperateType;
import com.zcode.zjw.timejob.common.JobOperateTypeEnum;
import com.zcode.zjw.timejob.common.JobParamsEntity;
import com.zcode.zjw.timejob.context.ITimeJobApplicationAdvice;
import com.zcode.zjw.timejob.union.infrastructure.exception.JobNotStartException;
import com.zcode.zjw.timejob.union.listener.event.BaseJobOperateEvent;
import com.zcode.zjw.timejob.union.listener.event.StopLivedWorkTaskEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledFuture;

/**
 * @author zhangjiwei
 * @description 任务停止操作事件
 * @date 2022/11/5 上午8:51
 */
@Component
@Slf4j
public class TimeJobStopOperateEvent implements BaseJobOperateEvent {

    @Resource
    private ITimeJobApplicationAdvice timeJobApplicationAdvice;

    /**
     * @author zhangjiwei
     * 功能描述: 关闭定时任务
     * @date 2022/8/3 2:14 下午
     */
    public void stopCron(JobParamsEntity jobParamsEntity) throws JobNotStartException {
        // 获取已经对应的future
        Map<String, ConcurrentLinkedQueue<ScheduledFuture<?>>> futureMap = timeJobApplicationAdvice.getJobFutureMap();
        ConcurrentLinkedQueue<ScheduledFuture<?>> futures = futureMap.get(jobParamsEntity.getTaskName());
        log.info("【定时任务】" + jobParamsEntity.getTaskName() + "任务正在尝试关闭中...");
        if (futures == null || futures.isEmpty()) {
            throw new JobNotStartException();
        }
        while (!futures.isEmpty()) {
            ScheduledFuture<?> future = futures.poll();
            while (!future.isCancelled()) {
                future.cancel(true);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // 停止当前工作任务线程
        timeJobApplicationAdvice.getTimeTaskPublisher().publish(new StopLivedWorkTaskEvent());
        // 执行Job结束回调
        Optional.ofNullable(jobParamsEntity.getJobFinishCallBack())
                .ifPresent((callBack) -> callBack.accept(null));
        log.info("【定时任务】" + jobParamsEntity.getTaskName() + "任务已关闭！");
    }

    @Override
    public void jobOperation(JobParamsEntity jobParamsEntity) {
        try {
            stopCron(jobParamsEntity);
        } catch (JobNotStartException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean supports(IJobOperateType jobOperateType) {
        return JobOperateTypeEnum.STOP.equals(jobOperateType);
    }
}
