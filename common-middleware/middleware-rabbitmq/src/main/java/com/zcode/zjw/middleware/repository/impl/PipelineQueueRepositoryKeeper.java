package com.zcode.zjw.middleware.repository.impl;

import com.zcode.zjw.middleware.manager.MqTypeEnum;
import com.zcode.zjw.middleware.repository.BasePipelineQueueRepository;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author zhangjiwei
 * @description 队列管道仓储者
 * @date 2023/2/14 下午12:29
 */
@Component
public class PipelineQueueRepositoryKeeper implements BasePipelineQueueRepository {

    @Autowired
    private ApplicationContext applicationContext;

    @Resource
    private ConcurrentHashMap<MqTypeEnum, CopyOnWriteArrayList<ConcurrentHashMap<String, Object>>> queueRepositoryContainer;

    @Override
    public <T> T searchPipelineQueue(MqTypeEnum mqType, String queuePipelineName) {
        // 如果不存在于Spring，返回null
        if (!applicationContext.containsBean(queuePipelineName)) {
            return null;
        }
        // 如果不存在于队列管道仓库里，返回null
        List<ConcurrentHashMap<String, Object>> collect = queueRepositoryContainer.get(mqType)
                .stream()
                .filter(map -> map.get(queuePipelineName) != null)
                .collect(Collectors.toList());
        if (collect.isEmpty()) {
            return null;
        }
        // 从本地仓库里获取
        for (MqTypeEnum mqTypeEnum : queueRepositoryContainer.keySet()) {
            // 如果该Mq列表不为空
            if (mqTypeEnum.equals(mqType) && !queueRepositoryContainer.get(mqType).isEmpty()) {
                for (Object queueMapObj : queueRepositoryContainer.get(mqType)) {
                    ConcurrentHashMap<String, Queue> queueMap = (ConcurrentHashMap<String, Queue>) queueMapObj;
                    if (queueMap.get(queuePipelineName) != null) {
                        return (T) queueMap.get(queuePipelineName);
                    }
                }
            }
        }

        return null;
    }

}
