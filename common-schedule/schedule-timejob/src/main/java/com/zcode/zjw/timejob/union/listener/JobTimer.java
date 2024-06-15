package com.zcode.zjw.timejob.union.listener;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.zcode.zjw.timejob.common.JobOperateTypeEnum;
import com.zcode.zjw.timejob.common.JobParamsEntity;
import com.zcode.zjw.timejob.common.WorkTaskLockEnum;
import com.zcode.zjw.timejob.context.ITimeJobApplicationAdvice;
import com.zcode.zjw.timejob.union.infrastructure.exception.JobNotNullException;
import com.zcode.zjw.timejob.union.infrastructure.factory.impl.TimeJobEventFactory;
import com.zcode.zjw.timejob.union.listener.event.TimeJobEvent;
import com.zcode.zjw.timejob.union.manager.impl.JobTransactionManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author zhangjiwei
 * @description 抽象定时Job
 * @date 2022/11/4 下午8:31
 */
@Component
public abstract class JobTimer implements ApplicationListener<TimeJobEvent>, BaseTimeJob {

    @Resource
    private TimeJobEventFactory timeTaskEventFactory;

    @Resource
    protected ITimeJobApplicationAdvice timeJobApplication;

    @Resource
    protected JobTransactionManager jobTransactionManager;

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 子类容器
     */
    private final ConcurrentHashMap<String, JobTimer> JobTimerServiceMap = new ConcurrentHashMap<>();

    /**
     * 定时任务运行逻辑
     */
    public abstract void runTask(Map<String, Object> params);

    /**
     * 注册实现类
     *
     * @return JobTimer实现类
     */
    public abstract Class<? extends JobTimer> register();

    @Resource
    private ConcurrentHashSet<String> jobStartRunSet;

    /**
     * 执行触发
     *
     * @return 是否执行逻辑
     */
    public abstract boolean trigger();


    @Override
    public void onApplicationEvent(TimeJobEvent timeTaskEvent) {
        // 去重（先将第一次发布的事件以任务ID形式注册到集合里，后续存在则不执行，除非重新启动）
        JobParamsEntity taskParamsEntity = timeTaskEvent.getTaskParamsEntity();
        if (taskParamsEntity.getTaskOperateType().equals(JobOperateTypeEnum.START)
                && !jobStartRunSet.contains(taskParamsEntity.getTaskId())) {
            jobStartRunSet.add(taskParamsEntity.getTaskId());
            innerMethod(timeTaskEvent);
        }
        // 如果是停止操作，将注册的ID删除
        else if (taskParamsEntity.getTaskOperateType().equals(JobOperateTypeEnum.STOP)) {
            jobStartRunSet.remove(taskParamsEntity.getTaskId());
            innerMethod(timeTaskEvent);
        }
    }

    private void innerMethod(TimeJobEvent timeTaskEvent) {
        try {
            Optional.ofNullable(timeTaskEvent)
                    .filter(taskEvent -> taskEvent.getTaskParamsEntity() != null)
                    .map(TimeJobEvent::getTaskParamsEntity)
                    .map(jobParamsEntity -> {
                        jobParamsEntity.setTaskFunc(jobFunc());
                        jobParamsEntity.setTaskOperateType(Optional.ofNullable(jobParamsEntity.getTaskOperateType()).orElse(JobOperateTypeEnum.START));
                        timeTaskEventFactory.getJobOperation(jobParamsEntity.getTaskOperateType())
                                .jobOperation(jobParamsEntity);
                        return jobParamsEntity;
                    }).orElseThrow(JobNotNullException::new);
        } catch (JobNotNullException e) {
            e.printStackTrace();
        }
    }

    /**
     * 任务自定义逻辑
     *
     * @return
     */
    private Consumer<JobParamsEntity> jobFunc() {
        return (jobParamsEntity) -> {
            // 如果满足触发后执行
            Optional.ofNullable(jobParamsEntity.getJobService())
                    .filter(jobTimerService -> timeTaskEventFactory.getJobTimerService(jobTimerService) != null)
                    .map(jobTimerService -> {
                        JobTimerServiceMap.put(jobParamsEntity.getTaskName(), timeTaskEventFactory.getJobTimerService(jobTimerService));
                        return jobTimerService;
                    })
                    .ifPresent(jobService -> chooseLockDeal(jobParamsEntity));
        };
    }

    /**
     * 选择不同的锁进行处理
     *
     * @param jobParamsEntity
     */
    private void chooseLockDeal(JobParamsEntity jobParamsEntity) {
        if (jobParamsEntity.getWorkTaskLock() == null) {
            noLockDeal(jobParamsEntity);
        } else if (jobParamsEntity.getWorkTaskLock().equals(WorkTaskLockEnum.OPTIMISTIC_LOCK)) {
            optimisticLockDeal(jobParamsEntity);
        } else if (jobParamsEntity.getWorkTaskLock().equals(WorkTaskLockEnum.MUTEX_LOCK)) {
            mutexLockDeal(jobParamsEntity);
        }
    }

    private void mutexLockDeal(JobParamsEntity jobParamsEntity) {
        Optional.ofNullable(jobParamsEntity)
                .filter(paramsEntity -> paramsEntity.getWorkTaskLock() != null)
                .map(JobParamsEntity::getWorkTaskLock)
                .filter(type -> type == WorkTaskLockEnum.MUTEX_LOCK)
                .ifPresent((type) -> {
                    try {
                        timeJobApplication.getMutexLock().lock();
                        JobTimer jobTimer = JobTimerServiceMap.get(jobParamsEntity.getTaskName());
                        if (jobTimer.register().equals(jobParamsEntity.getJobService()) && jobTimer.trigger()) {
                            jobTimer.runTask(jobParamsEntity.getParams());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        timeJobApplication.getMutexLock().unlock();
                    }
                });
    }

    private void optimisticLockDeal(JobParamsEntity jobParamsEntity) {
        Optional.ofNullable(jobParamsEntity)
                .filter(paramsEntity -> paramsEntity.getWorkTaskLock() != null)
                .map(JobParamsEntity::getWorkTaskLock)
                .filter(type -> type == WorkTaskLockEnum.OPTIMISTIC_LOCK)
                .ifPresent((type) -> {
                    JobTimer jobTimer = JobTimerServiceMap.get(jobParamsEntity.getTaskName());
                    if (jobTimer.register().equals(jobParamsEntity.getJobService()) && jobTimer.trigger()) {
                        jobTimer.runTask(jobParamsEntity.getParams());
                    }
                });
    }

    private void noLockDeal(JobParamsEntity jobParamsEntity) {
        Optional.ofNullable(jobParamsEntity)
                .filter(paramsEntity -> paramsEntity.getWorkTaskLock() == null)
                .ifPresent(paramsEntity -> {
                    JobTimer jobTimer = JobTimerServiceMap.get(paramsEntity.getTaskName());
                    if (jobTimer.register().equals(paramsEntity.getJobService()) && jobTimer.trigger()) {
                        jobTimer.runTask(jobParamsEntity.getParams());
                    }
                });
    }


}
