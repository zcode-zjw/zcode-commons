package com.zcode.zjw.middleware.factory.worker.exchange;

import com.zcode.zjw.common.utils.bean.DynamicRegisterBeanUtil;
import com.zcode.zjw.middleware.factory.queue.impl.RabbitPipelineQueue;
import com.zcode.zjw.middleware.factory.worker.IPipelineQueueFactoryWorker;
import com.zcode.zjw.middleware.utils.SpringContextUtils;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangjiwei
 * @description 交换机工作者接口
 * @date 2023/2/16 上午10:24
 */
public interface IExchangeWorkerQueue extends IPipelineQueueFactoryWorker {

    /**
     * 指导/领导工作
     *
     * @param rabbitMQueuePipeline 队列管道
     * @throws Exception
     */
    default void leadWork(RabbitPipelineQueue rabbitMQueuePipeline) throws Exception {
        // 注册队列
        registerQueue(rabbitMQueuePipeline);

        // 搜索已有的交换机
        if (rabbitMQueuePipeline.getNewExchange() == null || !rabbitMQueuePipeline.getNewExchange()) {
            searchBindExchange(rabbitMQueuePipeline);
        }
        // 注册一个交换机
        else {
            registerExchange(rabbitMQueuePipeline);
        }
        // 绑定队列与交换机
        bindQueueAndExchange(rabbitMQueuePipeline);
    }

    /**
     * 注册队列
     *
     * @param rabbitMQueuePipeline 队列管道
     */
    default void registerQueue(RabbitPipelineQueue rabbitMQueuePipeline) {
        String queuePipelineName = rabbitMQueuePipeline.getQueuePipelineName();
        List<Object> args = new ArrayList<>();
        args.add(queuePipelineName);
        if (rabbitMQueuePipeline.getDurable() != null) {
            args.add(rabbitMQueuePipeline.getDurable());
        }
        if (rabbitMQueuePipeline.getDurable() != null) {
            args.add(rabbitMQueuePipeline.getDurable());
        }
        if (rabbitMQueuePipeline.getAutoDelete() != null) {
            args.add(rabbitMQueuePipeline.getAutoDelete());
        }
        DynamicRegisterBeanUtil.registerBean((ConfigurableApplicationContext) SpringContextUtils.getApplicationContext(), queuePipelineName, Queue.class, args);
    }

    /**
     * 注册交换机
     *
     * @param rabbitMQueuePipeline 队列管道
     * @throws Exception
     */
    void registerExchange(RabbitPipelineQueue rabbitMQueuePipeline) throws Exception;

    /**
     * 检索绑定关系
     *
     * @param rabbitMQueuePipeline 队列管道
     */
    default void searchBindExchange(RabbitPipelineQueue rabbitMQueuePipeline) {
        if (!SpringContextUtils.getApplicationContext().containsBean(rabbitMQueuePipeline.getExchangeName())) {
            throw new NotFindExchangeException("交换机" + rabbitMQueuePipeline.getExchangeName() + "不存在 !");
        }
    }

    /**
     * 绑定队列与交换机
     *
     * @param rabbitMQueuePipeline 队列管道
     */
    default void bindQueueAndExchange(RabbitPipelineQueue rabbitMQueuePipeline) {
        String queuePipelineName = rabbitMQueuePipeline.getQueuePipelineName();
        String exchangeName = rabbitMQueuePipeline.getExchangeName();
        // 注入路由规则（绑定队列与交换机）
        String routerName = rabbitMQueuePipeline.getExchangeName() + "::" + rabbitMQueuePipeline.getQueuePipelineName();
        ApplicationContext applicationContext = SpringContextUtils.getApplicationContext();
        DynamicRegisterBeanUtil.registerBean((ConfigurableApplicationContext) applicationContext, routerName,
                Binding.class, () -> BindingBuilder.bind((Queue) SpringContextUtils.getBean(queuePipelineName))
                        .to((DirectExchange) applicationContext.getBean(exchangeName))
                        .with(rabbitMQueuePipeline.getRoutingKey()));
    }

    class NotFindExchangeException extends RuntimeException {
        NotFindExchangeException() {
            super();
        }

        NotFindExchangeException(String msg) {
            super(msg);
        }
    }

}
