package com.zcode.zjw.middleware.factory;

import com.zcode.zjw.middleware.factory.queue.BasePipelineQueue;
import com.zcode.zjw.middleware.manager.MqTypeEnum;

/**
 * @author zhangjiwei
 * @description 队列管道顶层接口
 * @date 2023/2/14 下午2:28
 */
public interface IPipelineQueueFactory {

    Object product(BasePipelineQueue queuePipeline) throws Exception;

    boolean support(MqTypeEnum mqType);

}
