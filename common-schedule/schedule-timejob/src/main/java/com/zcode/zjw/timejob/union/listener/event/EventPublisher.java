package com.zcode.zjw.timejob.union.listener.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author zhangjiwei
 * @description 事件发布接口
 * @date 2022/11/5 上午10:11
 */
public interface EventPublisher {

    /**
     * 发布事件
     *
     * @param event 事件
     */
    void publish(ApplicationEvent event);

}
