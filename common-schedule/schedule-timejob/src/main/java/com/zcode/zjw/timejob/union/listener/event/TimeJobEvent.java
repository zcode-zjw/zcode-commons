package com.zcode.zjw.timejob.union.listener.event;

import com.zcode.zjw.timejob.common.JobParamsEntity;
import org.springframework.context.ApplicationEvent;


/**
 * @author zhangjiwei
 * @description 定时任务开始事件
 * @date 2022/11/4 下午8:16
 */
public class TimeJobEvent extends ApplicationEvent {

    private static final long serialVersionUID = -2140890440135896011L;

    private JobParamsEntity jobParamsEntity;

    public TimeJobEvent(Object source, JobParamsEntity jobParamsEntity) {
        super(source);
        this.jobParamsEntity = jobParamsEntity;
    }

    public TimeJobEvent(Object source) {
        super(source);
        this.jobParamsEntity = (JobParamsEntity) source;
    }

    public JobParamsEntity getTaskParamsEntity() {
        return jobParamsEntity;
    }

    public void setJobParamsEntity(JobParamsEntity jobParamsEntity) {
        this.jobParamsEntity = jobParamsEntity;
    }
}
