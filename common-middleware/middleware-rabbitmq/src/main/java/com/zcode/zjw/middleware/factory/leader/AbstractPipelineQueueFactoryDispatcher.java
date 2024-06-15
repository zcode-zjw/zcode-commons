package com.zcode.zjw.middleware.factory.leader;

import com.zcode.zjw.middleware.factory.IPipelineQueueFactory;
import com.zcode.zjw.middleware.manager.MqTypeEnum;
import com.zcode.zjw.middleware.utils.ClassObjUtils;

import java.util.Set;

/**
 * @author zhangjiwei
 * @description 抽象队列管道工厂分发者
 * @date 2023/2/15 上午10:21
 */
public abstract class AbstractPipelineQueueFactoryDispatcher {

    /**
     * 根据mq类型选择工厂
     *
     * @param mqType mq类型
     * @return 工厂对象
     */
    public IPipelineQueueFactory selectFactory(MqTypeEnum mqType) throws IllegalAccessException, InstantiationException {
        // 获取子类
        Set<Class> subTypesOf = ClassObjUtils.getSubTypeList(IPipelineQueueFactory.class);
        for (Class aClass : subTypesOf) {
            IPipelineQueueFactory queuePipelineFactory = (IPipelineQueueFactory) aClass.newInstance();
            if (queuePipelineFactory.support(mqType)) {
                return queuePipelineFactory;
            }
        }
        return null;
    }

}
