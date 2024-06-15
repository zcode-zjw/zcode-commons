package com.zcode.zjw.middleware.factory.worker;

import com.zcode.zjw.middleware.factory.queue.BasePipelineQueue;
import com.zcode.zjw.middleware.factory.queue.impl.RabbitPipelineQueue;

/**
 * @author zhangjiwei
 * @description 队列管道工厂工作者接口
 * @date 2023/2/16 上午10:24
 */
public interface IPipelineQueueFactoryWorker {

    /**
     * 工作/加工
     *
     * @param queuePipeline 队列管道
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T work(BasePipelineQueue queuePipeline) throws Exception;

    /**
     * 领取加工材料
     *
     * @param rabbitMQueuePipeline 队列管道
     * @param <V>
     * @return
     */
    <V> V pickUp(RabbitPipelineQueue rabbitMQueuePipeline);

    boolean support(RabbitPipelineQueue.ExchangeType exchangeType);

}
