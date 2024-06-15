package com.zcode.zjw.log.user.common;


import lombok.Getter;

/**
 * 操作类型枚举
 */
@Getter
public enum OperationEnum {
    /**
     * 新建
     */
    ADD(1, "新建"),
    /**
     * 修改
     */
    MODIFY(2, "修改"),
    /**
     * 删除
     */
    DELETE(3, "删除"),
    /**
     * 导入
     */
    IMPORT(4, "导入"),
    /**
     * 登录
     */
    LOGIN(5, "登录"),
    /**
     * 登出
     */
    LOGOUT(6, "登出"),
    /**
     * 导出
     */
    EXPORT(7, "导出"),
    /**
     * 开启应用
     */
    START_APP(8, "开启应用"),
    /**
     * 停止应用
     */
    STOP_APP(9, "停止应用"),
    /**
     * 开启批量
     */
    START_BATCH(10001, "开启批量"),
    /**
     * 停止批量
     */
    STOP_BATCH(10002, "停止批量"),
    /**
     * 添加批量
     */
    ADD_BATCH(10003, "添加批量"),
    /**
     * 修改批量
     */
    UPDATE_BATCH(10004, "修改批量"),
    /**
     * 删除批量
     */
    DELETE_BATCH(10005, "删除批量"),
    /**
     * 开启作业
     */
    START_WORK(20001, "开启作业"),
    /**
     * 停止作业
     */
    STOP_WORK(20002, "停止作业"),
    /**
     * 添加作业
     */
    ADD_WORK(20003, "添加作业"),
    /**
     * 修改作业
     */
    UPDATE_WORK(20004, "修改作业"),
    /**
     * 删除作业
     */
    DELETE_WORK(20005, "删除作业"),
    ;

    private final Integer value;
    private final String operationType;

    OperationEnum(Integer value, String operationType) {
        this.value = value;
        this.operationType = operationType;
    }
    
}