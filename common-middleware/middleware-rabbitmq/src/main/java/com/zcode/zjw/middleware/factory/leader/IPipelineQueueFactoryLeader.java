package com.zcode.zjw.middleware.factory.leader;

import com.zcode.zjw.middleware.factory.worker.IPipelineQueueFactoryWorker;
import com.zcode.zjw.middleware.manager.MqTypeEnum;

import java.util.List;

/**
 * @author zhangjiwei
 * @description 队列管道工厂领导者接口
 * @date 2023/2/16 上午10:24
 */
public interface IPipelineQueueFactoryLeader {

    /**
     * 获取工作者名单
     *
     * @param mqType Mq类型
     * @return 工作者名单
     */
    List<IPipelineQueueFactoryWorker> getWorkerList(MqTypeEnum mqType, Class<?> workType) throws InstantiationException, IllegalAccessException;

}
