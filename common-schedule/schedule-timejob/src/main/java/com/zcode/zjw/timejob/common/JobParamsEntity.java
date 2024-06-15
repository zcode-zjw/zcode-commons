package com.zcode.zjw.timejob.common;

import cn.hutool.core.util.IdUtil;
import com.zcode.zjw.timejob.union.listener.JobTimer;
import com.zcode.zjw.timejob.util.GenerateCronExpressByType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Consumer;

/**
 * @author zhangjiwei
 * @description 任务参数实体
 * @date 2022/11/4 下午9:45
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobParamsEntity implements Serializable {

    private static final long serialVersionUID = 3812320301582714879L;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务事件
     */
    private String taskEvent;

    /**
     * cron表达式
     */
    private String cronString;

    /**
     * 任务时间
     */
    private int taskTime = 0;

    /**
     * 定时周期时间
     */
    private String taskCycleDatetimeString;

    /**
     * 定时周期类型
     */
    private GenerateCronExpressByType cycleType;

    /**
     * 任务时间对应的系统参数Value
     */
    private String sysParamValue;

    /**
     * 任务时间单位
     */
    private JobTimeUnitEnum taskTimeUnit;

    /**
     * 行为类型
     */
    private JobOperateTypeEnum taskOperateType;

    /**
     * 调度任务
     */
    private ScheduledFuture<?> future;

    /**
     * 任务函数
     */
    private Consumer<JobParamsEntity> taskFunc;

    /**
     * 执行延迟
     */
    private int delay;

    /**
     * 总执行次数
     */
    private Integer totalRunCount;

    /**
     * 单次执行次数
     */
    private Integer signalRunCount;

    /**
     * 单次执行延时
     */
    private Integer signalRunDelay;

    /**
     * 单次执行使用异步
     */
    private Boolean useAsyncToSignalRun;

    /**
     * 使用持久化
     */
    private Boolean usePersistent;

    /**
     * 使用的锁类型
     */
    private WorkTaskLockEnum workTaskLock;

    /**
     * 定时Job结束后回调
     */
    private Consumer<?> jobFinishCallBack;

    /**
     * 参数
     */
    private Map<String, Object> params;

    /**
     * 注册所属类
     *
     * @return
     */
    private Class<JobTimer> registerServiceClass;

    public String getTaskCycleDatetimeString() {
        return taskCycleDatetimeString;
    }

    public void setTaskCycleDatetimeString(String taskCycleDatetimeString) {
        this.taskCycleDatetimeString = taskCycleDatetimeString;
    }

    public GenerateCronExpressByType getCycleType() {
        return cycleType;
    }

    public void setCycleType(GenerateCronExpressByType cycleType) {
        this.cycleType = cycleType;
    }

    public Class<JobTimer> getRegisterServiceClass() {
        return registerServiceClass;
    }

    public void setRegisterServiceClass(Class<JobTimer> registerServiceClass) {
        this.registerServiceClass = registerServiceClass;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * 任务状态（0为初始化 1为运行中 2为关闭）
     */
    private Integer taskStatus;

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    /**
     * jobTimer服务类
     */
    private Class<? extends JobTimer> jobService;

    public static class Builder {
        JobParamsEntity jobParamsEntity = new JobParamsEntity();

        public Builder() {
        }

        public Builder setCycleType(GenerateCronExpressByType cycleType) {
            jobParamsEntity.cycleType = cycleType;
            return this;
        }

        public Builder setTaskCycleDatetimeString(String taskCycleDatetimeString) {
            jobParamsEntity.taskCycleDatetimeString = taskCycleDatetimeString;
            return this;
        }

        public Builder setTaskName(String taskName) {
            jobParamsEntity.taskName = taskName;
            return this;
        }

        public Builder setCronString(String cronString) {
            jobParamsEntity.cronString = cronString;
            return this;
        }

        public Builder setTaskTime(int taskTime) {
            jobParamsEntity.taskTime = taskTime;
            return this;
        }

        public Builder setRegisterServiceClass(Class<JobTimer> registerServiceClass) {
            jobParamsEntity.registerServiceClass = registerServiceClass;
            return this;
        }

        public Builder setSysParamValue(String sysParamValue) {
            jobParamsEntity.sysParamValue = sysParamValue;
            return this;
        }

        public Builder setTaskTimeUnit(JobTimeUnitEnum taskTimeUnit) {
            jobParamsEntity.taskTimeUnit = taskTimeUnit;
            return this;
        }

        public Builder setTaskOperateType(JobOperateTypeEnum taskOperateType) {
            jobParamsEntity.taskOperateType = taskOperateType;
            return this;
        }

        public Builder setDelay(int delay) {
            jobParamsEntity.delay = delay;
            return this;
        }

        public Builder setSignalRunCount(Integer signalRunCount) {
            jobParamsEntity.signalRunCount = signalRunCount;
            return this;
        }

        public Builder setTotalRunCount(Integer totalRunCount) {
            jobParamsEntity.totalRunCount = totalRunCount;
            return this;
        }

        public Builder setSignalRunDelay(Integer signalRunDelay) {
            jobParamsEntity.signalRunDelay = signalRunDelay;
            return this;
        }

        public Builder setUseAsyncToSignalRun(Boolean useAsyncToSignalRun) {
            jobParamsEntity.useAsyncToSignalRun = useAsyncToSignalRun;
            return this;
        }

        public Builder setUsePersistent(Boolean usePersistent) {
            jobParamsEntity.usePersistent = usePersistent;
            return this;
        }

        public Builder setWorkTaskLock(WorkTaskLockEnum workTaskLock) {
            jobParamsEntity.workTaskLock = workTaskLock;
            return this;
        }

        public Builder setFinishCallBack(Consumer<?> jobFinishCallBack) {
            jobParamsEntity.jobFinishCallBack = jobFinishCallBack;
            return this;
        }

        public Builder setTaskEvent(String taskEvent) {
            jobParamsEntity.taskEvent = taskEvent;
            return this;
        }

        public Builder setJobService(Class<? extends JobTimer> jobService) {
            jobParamsEntity.jobService = jobService;
            return this;
        }

        public Builder setParams(Map<String, Object> params) {
            jobParamsEntity.params = params;
            return this;
        }

        public Builder setTaskStatus(Integer taskStatus) {
            jobParamsEntity.taskStatus = taskStatus;
            return this;
        }

        public JobParamsEntity build() {
            jobParamsEntity.setTaskId(IdUtil.randomUUID());
            return jobParamsEntity;
        }
    }

    public Class<? extends JobTimer> getJobService() {
        return jobService;
    }

    public void setJobService(Class<? extends JobTimer> jobService) {
        this.jobService = jobService;
    }

    public String getTaskEvent() {
        return taskEvent;
    }

    public void setTaskEvent(String taskEvent) {
        this.taskEvent = taskEvent;
    }

    public Consumer<?> getJobFinishCallBack() {
        return jobFinishCallBack;
    }

    public void setJobFinishCallBack(Consumer<?> jobFinishCallBack) {
        this.jobFinishCallBack = jobFinishCallBack;
    }

    public WorkTaskLockEnum getWorkTaskLock() {
        return workTaskLock;
    }

    public void setWorkTaskLock(WorkTaskLockEnum workTaskLock) {
        this.workTaskLock = workTaskLock;
    }

    public Boolean getUsePersistent() {
        return usePersistent;
    }

    public void setUsePersistent(Boolean usePersistent) {
        this.usePersistent = usePersistent;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Boolean getUseAsyncToSignalRun() {
        return useAsyncToSignalRun;
    }

    public void setUseAsyncToSignalRun(Boolean useAsyncToSignalRun) {
        this.useAsyncToSignalRun = useAsyncToSignalRun;
    }

    public Integer getSignalRunDelay() {
        return signalRunDelay;
    }

    public void setSignalRunDelay(Integer signalRunDelay) {
        this.signalRunDelay = signalRunDelay;
    }

    public Integer getTotalRunCount() {
        return totalRunCount;
    }

    public void setTotalRunCount(Integer totalRunCount) {
        this.totalRunCount = totalRunCount;
    }

    public Integer getSignalRunCount() {
        return signalRunCount;
    }

    public void setSignalRunCount(Integer signalRunCount) {
        this.signalRunCount = signalRunCount;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getCronString() {
        return cronString;
    }

    public void setCronString(String cronString) {
        this.cronString = cronString;
    }

    public int getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(int taskTime) {
        this.taskTime = taskTime;
    }

    public String getSysParamValue() {
        return sysParamValue;
    }

    public void setSysParamValue(String sysParamValue) {
        this.sysParamValue = sysParamValue;
    }

    public JobTimeUnitEnum getTaskTimeUnit() {
        return taskTimeUnit;
    }

    public void setTaskTimeUnit(JobTimeUnitEnum taskTimeUnit) {
        this.taskTimeUnit = taskTimeUnit;
    }

    public JobOperateTypeEnum getTaskOperateType() {
        return taskOperateType;
    }

    public void setTaskOperateType(JobOperateTypeEnum taskOperateType) {
        this.taskOperateType = taskOperateType;
    }

    public ScheduledFuture<?> getFuture() {
        return future;
    }

    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
    }

    public Consumer<JobParamsEntity> getTaskFunc() {
        return taskFunc;
    }

    public void setTaskFunc(Consumer<JobParamsEntity> taskFunc) {
        this.taskFunc = taskFunc;
    }
}
