package com.zcode.zjw.middleware.factory.impl;

import com.zcode.zjw.middleware.factory.queue.BasePipelineQueue;
import com.zcode.zjw.middleware.factory.IPipelineQueueFactory;
import com.zcode.zjw.middleware.factory.leader.impl.PipelineQueueFactoryLeader;
import com.zcode.zjw.middleware.factory.queue.impl.RabbitPipelineQueue;
import com.zcode.zjw.middleware.factory.worker.exchange.IExchangeWorkerQueue;
import com.zcode.zjw.middleware.manager.MqTypeEnum;
import com.zcode.zjw.middleware.utils.SpringContextUtils;
import org.springframework.amqp.core.Queue;

/**
 * @author zhangjiwei
 * @description RabbitMq队列管道生产工厂
 * @date 2023/2/14 下午2:35
 */
public class RabbitPipelineQueueFactory implements IPipelineQueueFactory {

    @Override
    public Queue product(BasePipelineQueue queuePipeline) throws Exception {
        RabbitPipelineQueue rabbitMQueuePipeline = (RabbitPipelineQueue) queuePipeline;
        String queuePipelineName = queuePipeline.getQueuePipelineName();

        PipelineQueueFactoryLeader queuePipelineFactoryLeader = new PipelineQueueFactoryLeader();
        for (Object worker : queuePipelineFactoryLeader.getWorkerList(MqTypeEnum.RABBIT_MQ, IExchangeWorkerQueue.class)) {
            IExchangeWorkerQueue exchangeWorker = (IExchangeWorkerQueue) worker;
            if (exchangeWorker.support(rabbitMQueuePipeline.getExchangeType())) {
                exchangeWorker.work(queuePipeline);
            }
        }

        return (Queue) SpringContextUtils.getBean(queuePipelineName);

    }

    @Override
    public boolean support(MqTypeEnum mqType) {
        return MqTypeEnum.RABBIT_MQ.equals(mqType);
    }

}
