package com.zcode.zjw.middleware.factory.queue;


import com.zcode.zjw.middleware.factory.queue.impl.RabbitPipelineQueue;
import com.zcode.zjw.middleware.manager.MqTypeEnum;
import lombok.*;

/**
 * @author zhangjiwei
 * @description 队列管道产品父类
 * @date 2023/2/14 下午2:33
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BasePipelineQueue {

    /**
     * 队列管道名称
     */
    String queuePipelineName;

    /**
     * Mq类型
     */
    MqTypeEnum mqType;

    /**
     * RabbitMq队列管道产品
     */
    RabbitPipelineQueue rabbitMQueuePipeline;

    public String getQueuePipelineName() {
        return queuePipelineName;
    }

    public void setQueuePipelineName(String queuePipelineName) {
        this.queuePipelineName = queuePipelineName;
    }

    public MqTypeEnum getMqType() {
        return mqType;
    }

    public void setMqType(MqTypeEnum mqType) {
        this.mqType = mqType;
    }

    public RabbitPipelineQueue getRabbitMQueuePipeline() {
        return rabbitMQueuePipeline;
    }

    public void setRabbitMQueuePipeline(RabbitPipelineQueue rabbitMQueuePipeline) {
        this.rabbitMQueuePipeline = rabbitMQueuePipeline;
    }
}
