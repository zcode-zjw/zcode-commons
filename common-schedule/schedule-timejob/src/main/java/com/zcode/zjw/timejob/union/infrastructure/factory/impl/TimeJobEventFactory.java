package com.zcode.zjw.timejob.union.infrastructure.factory.impl;

import com.zcode.zjw.timejob.common.IJobOperateType;
import com.zcode.zjw.timejob.union.infrastructure.factory.EventLoader;
import com.zcode.zjw.timejob.union.infrastructure.factory.JobEventFactory;
import com.zcode.zjw.timejob.union.listener.JobTimer;
import com.zcode.zjw.timejob.union.listener.event.BaseJobOperateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zhangjiwei
 * @description 定时任务事件工厂
 * @date 2022/11/5 上午8:27
 */
@Component
public class TimeJobEventFactory implements JobEventFactory {

    private final EventLoader eventLoader;

    @Resource
    private ApplicationContext applicationContext;

    @Autowired
    public TimeJobEventFactory(EventLoader eventLoader) {
        this.eventLoader = eventLoader;
    }

    /**
     * 获取任务操作对象
     *
     * @param jobOperateType 任务操作类型
     * @return 任务操作对象
     */
    public BaseJobOperateEvent getJobOperation(IJobOperateType jobOperateType) {
        Map<String, BaseJobOperateEvent> jobOperateMap = eventLoader.getJobOperateMap();
        AtomicReference<BaseJobOperateEvent> result = new AtomicReference<>();
        jobOperateMap.forEach((name, obj) -> {
            if (obj.supports(jobOperateType)) {
                result.set(obj);
            }
        });
        return result.get();
    }

    public JobTimer getJobTimerService(Class<? extends JobTimer> jobTimerServiceClazz) {
        AtomicReference<JobTimer> res = new AtomicReference<>();
        Map<String, JobTimer> jobTimerServiceMap = applicationContext.getBeansOfType(JobTimer.class);
        jobTimerServiceMap.forEach((name, obj) -> {
            String currentObjName = obj.getClass().getName().split("\\$\\$")[0];
            String targetName = jobTimerServiceClazz.getName();
            if (currentObjName.equals(targetName)) {
                res.set(obj);
            }
        });
        return res.get();
    }

}
