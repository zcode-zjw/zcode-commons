package com.zcode.zjw.timejob.union.handler.impl;

import com.zcode.zjw.timejob.common.JobParamsEntity;
import com.zcode.zjw.timejob.union.handler.TaskExecuteChainHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;

/**
 * @author zhangjiwei
 * @description 选择同/异步模式处理
 * @date 2022/11/6 上午9:39
 */
@Component
@Slf4j
public class StartTaskASyncModeHandler extends StartTaskExecuteChainHandler {

    @Override
    public ScheduledFuture<?> execute(JobParamsEntity jobParamsEntity) {
        log.info("【定时任务】" + jobParamsEntity.getTaskName() + "任务开始执行");
        int runCount = Optional.ofNullable(jobParamsEntity.getSignalRunCount()).orElse(1);
        Boolean isUseAsync = Optional.ofNullable(jobParamsEntity.getUseAsyncToSignalRun()).orElse(Boolean.FALSE);
        // 轮询单次执行次数
        for (int i = 0; i < runCount; i++) {
            if (isUseAsync) {
                CompletableFuture.supplyAsync(() -> {
                    timeJobApplicationAdvice.getWorkTaskLivedExecutor().add(Thread.currentThread());
                    // 执行业务
                    jobParamsEntity.getTaskFunc().accept(jobParamsEntity);
                    return null;
                }, timeJobApplicationAdvice.getJobScheduler());
            } else {
                jobParamsEntity.getTaskFunc().accept(jobParamsEntity);
            }
        }
        return null;
    }

    @Override
    public Class<? extends TaskExecuteChainHandler> nextHandler() {
        return null;
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
        return handler.getClass().equals(StartTaskASyncModeHandler.class);
    }
}
