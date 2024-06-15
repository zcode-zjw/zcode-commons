package com.zcode.zjw.timejob.union.infrastructure.factory;


import com.zcode.zjw.timejob.common.IJobOperateType;
import com.zcode.zjw.timejob.union.listener.event.BaseJobOperateEvent;

/**
 * @author zhangjiwei
 * @description 任务事件工厂
 * @date 2022/11/5 上午8:26
 */
public interface JobEventFactory {

    /**
     * 获取任务操作事件
     * @param taskOperateType 任务操作类型
     * @return 任务操作事件
     */
    BaseJobOperateEvent getJobOperation(IJobOperateType taskOperateType);

}
