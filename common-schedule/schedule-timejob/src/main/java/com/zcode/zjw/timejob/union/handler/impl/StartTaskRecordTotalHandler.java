package com.zcode.zjw.timejob.union.handler.impl;

import com.zcode.zjw.timejob.common.JobParamsEntity;
import com.zcode.zjw.timejob.common.JobTimeUnitEnum;
import com.zcode.zjw.timejob.union.handler.TaskExecuteChainHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledFuture;

/**
 * @author zhangjiwei
 * @description 记录执行总数相关信息
 * @date 2022/11/6 上午9:39
 */
@Component
@Slf4j
public class StartTaskRecordTotalHandler extends StartTaskExecuteChainHandler {

    @Override
    public ScheduledFuture<?> execute(JobParamsEntity jobParamsEntity) {
        paramsChooseDeal(jobParamsEntity);
        return null;
    }

    /**
     * 对用户传入的任务参数处理
     *
     * @param jobParamsEntity
     */
    private void paramsChooseDeal(JobParamsEntity jobParamsEntity) {
        JobTimeUnitEnum taskTimeUnit = Optional.ofNullable(jobParamsEntity.getTaskTimeUnit())
                .orElse(JobTimeUnitEnum.SECOND);
        // 延迟等待
        log.info("===================================================== " + jobParamsEntity.getTaskName() + "===================================================== ");
        log.info("【定时任务】" + jobParamsEntity.getTaskName() + "正在等待开始...");
        try {
            Thread.sleep((long) jobParamsEntity.getDelay() * taskTimeUnit.getSecondNum() * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 如果已经达到最大执行次数，退出
        Optional.ofNullable(jobParamsEntity).map((paramsEntity) -> {
            Optional.ofNullable(paramsEntity.getTotalRunCount())
                    .filter(totalRunCount -> totalRunCount == 0).map(totalRunCount -> {
                log.info("【定时任务】" + jobParamsEntity.getTaskName() + "任务已达最大执行次数，正在停止...");
                ConcurrentLinkedQueue<ScheduledFuture<?>> currentFutures = timeJobApplicationAdvice.getJobFutureMap().get(jobParamsEntity.getTaskName());
                while (!currentFutures.isEmpty()) {
                    ScheduledFuture<?> future = currentFutures.poll();
                    while (!future.isCancelled()) {
                        future.cancel(true);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // 执行Job结束后回调
                Optional.ofNullable(jobParamsEntity.getJobFinishCallBack()).ifPresent(callBack -> callBack.accept(null));
                log.info("【定时任务】" + jobParamsEntity.getTaskName() + "任务已停止");
                return totalRunCount;
            }).orElseGet(() -> {
                if (jobParamsEntity.getTotalRunCount() != null) {
                    jobParamsEntity.setTotalRunCount(jobParamsEntity.getTotalRunCount() - 1);
                }
                return null;
            });
            return paramsEntity;
        });
    }

    @Override
    public Class<StartTaskASyncModeHandler> nextHandler() {
        return StartTaskASyncModeHandler.class;
    }

    @Override
    public Class<StartTaskRecordTotalHandler> beforeHandler() {
        return StartTaskRecordTotalHandler.class;
    }

    @Override
    public int groupSortFlag() {
        return 0;
    }

    @Override
    public boolean supports(TaskExecuteChainHandler handler) {
        return handler.getClass().equals(StartTaskRecordTotalHandler.class);
    }
}
