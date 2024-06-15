package com.zcode.zjw.middleware.factory.leader.impl;

import com.zcode.zjw.middleware.factory.leader.IPipelineQueueFactoryLeader;
import com.zcode.zjw.middleware.factory.worker.IPipelineQueueFactoryWorker;
import com.zcode.zjw.middleware.manager.MqTypeEnum;
import com.zcode.zjw.middleware.utils.ClassObjUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangjiwei
 * @description 队列管道工厂领导者
 * @date 2023/2/16 上午10:49
 */
public class PipelineQueueFactoryLeader implements IPipelineQueueFactoryLeader {

    @Override
    public List getWorkerList(MqTypeEnum mqType, Class<?> workType) throws InstantiationException, IllegalAccessException {
        List<IPipelineQueueFactoryWorker> workerList = new ArrayList<>();
        for (MqTypeEnum value : MqTypeEnum.values()) {
            if (value.equals(mqType)) {
                return ClassObjUtils.getSubInstanceList(workType);
            }
        }
        return workerList;
    }


}
