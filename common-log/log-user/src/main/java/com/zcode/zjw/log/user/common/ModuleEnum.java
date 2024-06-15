package com.zcode.zjw.log.user.common;

import lombok.Getter;

/**
 * 系统模块枚举类
 * 可以根据自己系统的实际情况增添模块
 */
@Getter
public enum ModuleEnum {
    /**
     * 监控管理
     */
    MINOR("监控管理"),
    /**
     * 运维小工具
     */
    TOOLS("运维小工具"),
    /**
     * 服务参数配置
     */
    SERVICE_PARAM_CONFIG("服务参数配置"),
    /**
     * 个人设置
     */
    USER_SETTING("个人设置"),
    /**
     * 用户
     */
    USER("用户"),
    /**
     * 消息
     */
    MESSAGE("消息"),

    ETL_SCHEDULE_MANAGER("ETL-调度管理"),

    ETL_SCHEDULE_ENGINE("ETL-调度引擎"),

    ;

    private final String moduleCode;

    ModuleEnum(String moduleCode) {
        this.moduleCode = moduleCode;
    }

}