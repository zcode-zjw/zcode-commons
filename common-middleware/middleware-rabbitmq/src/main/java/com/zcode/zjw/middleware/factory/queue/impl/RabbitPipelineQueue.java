package com.zcode.zjw.middleware.factory.queue.impl;

import com.zcode.zjw.middleware.factory.queue.BasePipelineQueue;
import com.zcode.zjw.middleware.manager.MqTypeEnum;
import lombok.*;

/**
 * @author zhangjiwei
 * @description RabbitMq队列管道产品
 * @date 2023/2/14 下午2:35
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString
@Builder
@Data
@AllArgsConstructor
public class RabbitPipelineQueue extends BasePipelineQueue {

    // 交换机类型
    public enum ExchangeType {
        DIRECT,
        TOPIC,
        FANOUT;
    }

    public RabbitPipelineQueue(ExchangeType exchangeType, String exchangeName, String routingKey, Boolean durable,
                               Boolean autoDelete, Boolean newExchange) {
        this.exchangeType = exchangeType;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.durable = durable;
        this.autoDelete = autoDelete;
        this.newExchange = newExchange;
    }

    public RabbitPipelineQueue(ExchangeType exchangeType, String exchangeName, String routingKey, Boolean durable,
                               Boolean autoDelete) {
        this.exchangeType = exchangeType;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.durable = durable;
        this.autoDelete = autoDelete;
    }

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

    /**
     * 交换机类型
     */
    private ExchangeType exchangeType;

    /**
     * 交换机名称
     */
    private String exchangeName;

    /**
     * 绑定键
     */
    private String routingKey;

    /**
     * 是否持久化
     */
    private Boolean durable;

    /**
     * 是否自动删除
     */
    private Boolean autoDelete;

    /**
     * 是否新建交换机
     */
    private Boolean newExchange;

}
