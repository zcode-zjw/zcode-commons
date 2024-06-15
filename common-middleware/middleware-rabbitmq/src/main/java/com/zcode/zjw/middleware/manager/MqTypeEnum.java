package com.zcode.zjw.middleware.manager;


import com.rabbitmq.client.AMQP;

/**
 * @author zhangjiwei
 * @description 队列类型枚举
 * @date 2023/2/14 上午11:59
 */
public enum MqTypeEnum {
    RABBIT_MQ(AMQP.Queue.class, "rabbit_mq"),
    ROCKET_MQ(Object.class, "rocket_mq")
    ;

    private Class<?> type;

    private String desc;

    MqTypeEnum(Class<?> type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Class<?> getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
