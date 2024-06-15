package com.zcode.zjw.log.trace.infrastructure.common.event;

import org.springframework.context.ApplicationEvent;

public interface EventPublisher {

    void publish(ApplicationEvent event);

}