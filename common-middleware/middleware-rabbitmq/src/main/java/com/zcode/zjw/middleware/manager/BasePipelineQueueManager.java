package com.zcode.zjw.middleware.manager;

import com.zcode.zjw.middleware.factory.queue.BasePipelineQueue;

/**
 * @author zhangjiwei
 * @description 队列管道管理者接口
 * @date 2023/2/14 上午11:39
 */
public interface BasePipelineQueueManager {

    /**
     * 获取队列管道
     *
     * @param mqType            Mq类型
     * @param queuePipelineName 队列通道名称
     * @return 队列管道
     * @throws Exception
     */
    <T> T getPipelineQueue(MqTypeEnum mqType, String queuePipelineName) throws Exception;

    /**
     * 添加队列管道
     *
     * @param queuePipeline 队列管道
     */
    void addPipelineQueue(BasePipelineQueue queuePipeline) throws Exception;

    /**
     * 获取操作客户端
     *
     * @param mqTypeEnum Mq类型枚举
     * @param <T> 指定返回值类型
     * @return 客户端
     */
    <T> T getClient(MqTypeEnum mqTypeEnum);

}
