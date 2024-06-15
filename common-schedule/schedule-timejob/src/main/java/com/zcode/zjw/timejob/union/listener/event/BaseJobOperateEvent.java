package com.zcode.zjw.timejob.union.listener.event;


import com.zcode.zjw.timejob.common.IJobOperateType;
import com.zcode.zjw.timejob.common.JobParamsEntity;

/**
 * @author zhangjiwei
 * @description 任务操作事件接口
 * @date 2022/11/5 上午8:32
 */
public interface BaseJobOperateEvent {

    /**
     * 任务操作方法
     *
     * @param jobParamsEntity 任务参数实体
     */
    void jobOperation(JobParamsEntity jobParamsEntity);

    boolean supports(IJobOperateType jobOperateType);

}
