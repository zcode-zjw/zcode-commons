package com.zcode.zjw.timejob.union.listener.event.impl;

import com.zcode.zjw.timejob.common.IJobOperateType;
import com.zcode.zjw.timejob.common.JobOperateTypeEnum;
import com.zcode.zjw.timejob.common.JobParamsEntity;
import com.zcode.zjw.timejob.common.JobTimeUnitEnum;
import com.zcode.zjw.timejob.union.handler.impl.StartTaskCronStringHandler;
import com.zcode.zjw.timejob.union.listener.event.BaseJobOperateEvent;
import com.zcode.zjw.timejob.util.CronUtils;
import com.zcode.zjw.timejob.util.GenerateCronExpressByType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zhangjiwei
 * @description 定时任务开始操作事件
 * @date 2022/11/5 上午8:51
 */
@Slf4j
@Component
public class TimeJobStartOperateEvent implements BaseJobOperateEvent {

    @Resource
    private ThreadPoolTaskScheduler threadPoolTimeTaskScheduler;

    @Resource
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<ScheduledFuture<?>>> taskFutureMap;

    @Resource
    private ThreadPoolExecutor taskThreadPoolExecutor;

    @Resource
    private StartTaskCronStringHandler startTaskCronStringHandler;

    /**
     * @return future
     * @author zhangjiwei
     * 功能描述: 开启定时任务
     * @date 2022/8/3 2:15 下午
     */
    public ScheduledFuture<?> startCron(JobParamsEntity jobParamsEntity) {
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
            if (null != jobParamsEntity.getCycleType() && null != jobParamsEntity.getTaskCycleDatetimeString()) {
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
        if (null == cronString) {
            throw new RuntimeException("无法生成cron表达式，请选择定时类型（周期或间隔）和对应的参数 -> " + jobParamsEntity.toString());
        }
        return threadPoolTimeTaskScheduler.schedule(() -> {
            Optional.ofNullable(startTaskCronStringHandler.getHandlers())
                    // 选择Handler处理
                    .ifPresent((handlers) -> handlers.forEach((handler) -> {
                        if (handler.supports(handler)) {
                            handler.execute(jobParamsEntity);
                        }
                    }));
            log.info("【定时任务】" + jobParamsEntity.getTaskName() + "任务执行成功！");
        }, new CronTrigger(cronString));
    }

    @Override
    public void jobOperation(JobParamsEntity jobParamsEntity) {
        ScheduledFuture<?> currentFuture = startCron(jobParamsEntity);
        if (currentFuture != null) {
            // 如果该任务没有加入
            if (!taskFutureMap.containsKey(jobParamsEntity.getTaskName())) {
                ConcurrentLinkedQueue<ScheduledFuture<?>> futureList = new ConcurrentLinkedQueue<>();
                futureList.add(currentFuture);
                taskFutureMap.put(jobParamsEntity.getTaskName(), futureList);
            } else {
                // 否则直接添加到对应的列表里
                taskFutureMap.get(jobParamsEntity.getTaskName()).add(currentFuture);
            }
            boolean usePersistent = Optional.ofNullable(jobParamsEntity.getUsePersistent()).orElse(false);
        }
    }

    @Override
    public boolean supports(IJobOperateType jobOperateType) {
        return JobOperateTypeEnum.START.equals(jobOperateType);
    }

}
