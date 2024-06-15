package com.zcode.zjw.timejob.union.listener.event.impl;

import com.zcode.zjw.timejob.union.listener.event.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * @author zhangjiwei
 * @description 定时任务事件发表者
 * @date 2022/11/5 上午10:13
 */
@Component
public class TimeJobPublisher implements EventPublisher {

    private final ApplicationContext applicationContext;

    @Autowired
    public TimeJobPublisher(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void publish(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }
}
