package com.zcode.zjw.timejob.union.handler.impl;

import com.zcode.zjw.timejob.common.JobParamsEntity;
import com.zcode.zjw.timejob.common.JobTimeUnitEnum;
import com.zcode.zjw.timejob.union.handler.TaskExecuteChainHandler;
import com.zcode.zjw.timejob.util.CronUtils;
import com.zcode.zjw.timejob.util.GenerateCronExpressByType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

/**
 * @author zhangjiwei
 * @description 获取Cron字符串
 * @date 2022/11/6 上午9:34
 */
@Component
public class StartTaskCronStringHandler extends StartTaskExecuteChainHandler {

    @Override
    public ScheduledFuture<?> execute(JobParamsEntity jobParamsEntity) {
        String cronString = null;
        // 如果未传入时间单位，默认使用秒数
        JobTimeUnitEnum taskTimeUnit = Optional.ofNullable(jobParamsEntity.getTaskTimeUnit())
                .orElse(JobTimeUnitEnum.SECOND);
        if (jobParamsEntity.getCronString() == null) {
            // 根据间隔时间定时
            if (null != jobParamsEntity.getTaskTimeUnit() && 0 != jobParamsEntity.getTaskTime()) {
                // 根据传入的时间单位计算最后定时时间
                //int cronNum = jobParamsEntity.getTaskTime() * taskTimeUnit.getSecondNum();
                //cronString = CronConvertUtil.convert(String.valueOf(cronNum)).get();
                cronString = CronUtils.generateCronString(jobParamsEntity.getTaskTime(), taskTimeUnit);
            }
            // 根据周期时间定时（如果同时含有间隔时间和周期时间，则周期时间会覆盖间隔时间，即优先级：周期 > 间隔）
            if (null != jobParamsEntity.getCycleType()) {
                Map<String, String> cronExpressionByCycleMap = GenerateCronExpressByType.getCronExpressionByCycleType(
                        jobParamsEntity.getTaskCycleDatetimeString(), jobParamsEntity.getCycleType()
                );
                cronString = cronExpressionByCycleMap.get("expression");
            }
        }
        // 如果存在cron字符串，优先选择
        else {
            cronString = jobParamsEntity.getCronString();
        }
        jobParamsEntity.setCronString(cronString);
        return null;
    }

    @Override
    public Class<StartTaskRecordTotalHandler> nextHandler() {
        return StartTaskRecordTotalHandler.class;
    }

    @Override
    public Class<? extends TaskExecuteChainHandler> beforeHandler() {
        return null;
    }

    @Override
    public int groupSortFlag() {
        return 0;
    }

    @Override
    public boolean supports(TaskExecuteChainHandler handler) {
        return handler.getClass().equals(StartTaskCronStringHandler.class);
    }
}
