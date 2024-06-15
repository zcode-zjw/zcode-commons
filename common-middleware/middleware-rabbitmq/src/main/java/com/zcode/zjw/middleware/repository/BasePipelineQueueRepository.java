package com.zcode.zjw.middleware.repository;

import com.zcode.zjw.middleware.manager.MqTypeEnum;

/**
 * @author zhangjiwei
 * @description 队列管道仓储服务接口
 * @date 2023/2/14 下午12:27
 */
public interface BasePipelineQueueRepository {

    /**
     * 从本地仓储里查找队列管道
     *
     * @param mqType            Mq类型
     * @param queuePipelineName 队列管道名称
     * @return 队列管道（无就返回null）
     * @throws Exception
     */
    <T> T searchPipelineQueue(MqTypeEnum mqType, String queuePipelineName) throws Exception;

}
