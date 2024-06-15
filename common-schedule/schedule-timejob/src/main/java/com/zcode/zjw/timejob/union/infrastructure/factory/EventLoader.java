package com.zcode.zjw.timejob.union.infrastructure.factory;

import com.zcode.zjw.timejob.common.IJobOperateType;
import com.zcode.zjw.timejob.union.listener.event.BaseJobOperateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangjiwei
 * @description 任务事件加载者
 * @date 2022/11/5 上午8:37
 */
@Component("eventLoader")
public class EventLoader {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 获取任务操作map
     *
     * @return map
     */
    public Map<String, BaseJobOperateEvent> getJobOperateMap() {
        return applicationContext.getBeansOfType(BaseJobOperateEvent.class);
    }

    /**
     * 获取任务操作类型map
     *
     * @return map
     */
    public Map<String, IJobOperateType> getJobOperaTypeMap() {
        Map<String, IJobOperateType> result = new HashMap<>();
        applicationContext.getBeansOfType(IJobOperateType.class).forEach((name, value) -> value.getTypeList().forEach((type) -> result.put(name, type)));
        return result;
    }

}
