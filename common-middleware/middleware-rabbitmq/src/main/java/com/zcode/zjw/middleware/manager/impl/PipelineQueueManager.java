package com.zcode.zjw.middleware.manager.impl;

import com.zcode.zjw.middleware.factory.leader.impl.PipelineQueueFactoryDispatcher;
import com.zcode.zjw.middleware.factory.queue.BasePipelineQueue;
import com.zcode.zjw.middleware.manager.BasePipelineQueueManager;
import com.zcode.zjw.middleware.manager.MqTypeEnum;
import com.zcode.zjw.middleware.repository.BasePipelineQueueRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author zhangjiwei
 * @description 队列管道管理者
 * @date 2023/2/14 上午11:56
 */
@Component
public class PipelineQueueManager implements BasePipelineQueueManager {

    @Resource
    private BasePipelineQueueRepository queuePipelineRepositoryKeeper;

    @Resource
    private ConcurrentHashMap<MqTypeEnum, CopyOnWriteArrayList<ConcurrentHashMap<String, Object>>> queueRepositoryContainer;

    @Resource
    private PipelineQueueFactoryDispatcher queuePipelineFactoryDispatcher;

    @Override
    public <T> T getPipelineQueue(MqTypeEnum mqType, String queuePipelineName) throws Exception {
        // 从仓储服务获取
        return (T) queuePipelineRepositoryKeeper.searchPipelineQueue(mqType, queuePipelineName);
    }

    @Override
    public void addPipelineQueue(BasePipelineQueue queuePipeline) throws Exception {
        // 校验参数
        if (queuePipeline.getMqType() == null || queuePipeline.getQueuePipelineName() == null) {
            throw new Exception("mqType或queuePipelineName参数不能为空！");
        }
        // 先查询一下是否已经存在该队列管道
        if (getPipelineQueue(queuePipeline.getMqType(), queuePipeline.getQueuePipelineName()) != null) {
            throw new Exception("该队列管道已经存在：" + queuePipeline);
        }
        // 让工厂生产一个
        MqTypeEnum mqType = queuePipeline.getMqType();
        String queuePipelineName = queuePipeline.getQueuePipelineName();
        Object newQueuePipeline = queuePipelineFactoryDispatcher.selectFactory(mqType).product(queuePipeline);
        // 存入仓库里
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put(queuePipelineName, newQueuePipeline);
        queueRepositoryContainer.get(mqType).add(map);
    }

    public ConcurrentHashMap<MqTypeEnum, CopyOnWriteArrayList<ConcurrentHashMap<String, Object>>> getQueueRepositoryContainer() {
        return queueRepositoryContainer;
    }


    @Autowired
    private RabbitTemplate rabbitMqClient;

    /**
     * 获取Mq操作客户端
     *
     * @param mqTypeEnum
     * @return
     */
    public <T> T getClient(MqTypeEnum mqTypeEnum) {
        if (MqTypeEnum.RABBIT_MQ.equals(mqTypeEnum)) {
            return (T) rabbitMqClient;
        } else if (MqTypeEnum.ROCKET_MQ.equals(mqTypeEnum)) {
            return null;
        }
        throw new RuntimeException("MQ队列客户端不存在: " + mqTypeEnum);
    }

}
