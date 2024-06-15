package com.zcode.zjw.middleware.factory.worker.exchange.impl;

import com.zcode.zjw.common.utils.bean.DynamicRegisterBeanUtil;
import com.zcode.zjw.middleware.factory.queue.BasePipelineQueue;
import com.zcode.zjw.middleware.factory.queue.impl.RabbitPipelineQueue;
import com.zcode.zjw.middleware.factory.worker.exchange.IExchangeWorkerQueue;
import com.zcode.zjw.middleware.utils.SpringContextUtils;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangjiwei
 * @description 直连交换机工作者
 * @date 2023/2/16 上午10:28
 */
public class DirectExchangeWorkerQueue implements IExchangeWorkerQueue {

    @Override
    public <T> T work(BasePipelineQueue queuePipeline) throws Exception {
        leadWork((RabbitPipelineQueue) queuePipeline);
        return null;
    }

    @Override
    public <V> V pickUp(RabbitPipelineQueue rabbitMQueuePipeline) {
        // 构造参数
        List<Object> args = new ArrayList<>();
        args.add(rabbitMQueuePipeline.getExchangeName());
        if (rabbitMQueuePipeline.getDurable() != null) {
            args.add(rabbitMQueuePipeline.getDurable());
        }
        if (rabbitMQueuePipeline.getAutoDelete() != null) {
            args.add(rabbitMQueuePipeline.getAutoDelete());
        }
        return (V) args;
    }

    @Override
    public void registerExchange(RabbitPipelineQueue rabbitMQueuePipeline) throws Exception {
        String exchangeName = rabbitMQueuePipeline.getExchangeName();
        // 构造参数
        List<Object> args = pickUp(rabbitMQueuePipeline);
        // 注册交换机
        DynamicRegisterBeanUtil.registerBean((ConfigurableApplicationContext) SpringContextUtils.getApplicationContext(), exchangeName, DirectExchange.class, args);
        if (!SpringContextUtils.getApplicationContext().containsBean(exchangeName)) {
            throw new Exception("生产交换机失败！");
        }
    }

    @Override
    public boolean support(RabbitPipelineQueue.ExchangeType exchangeType) {
        return RabbitPipelineQueue.ExchangeType.DIRECT.equals(exchangeType);
    }

}
