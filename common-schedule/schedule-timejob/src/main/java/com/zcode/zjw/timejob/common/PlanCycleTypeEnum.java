package com.zcode.zjw.timejob.common;

/**
 * @author : zjw
 * @description : 质控计划周期类型映射
 * @since : 2022/8/4 14:11
 */
public enum PlanCycleTypeEnum {
    SECOND("second", "秒"),

    MINUTE("minute", "分钟"),

    HOUR("hour", "小时"),

    DAY("day", "日"),

    WEEK("week", "周"),

    MONTH("month", "月"),

    QUARTER("quarter", "季度"),

    YEAR("year", "年");


    /**
     * 周期类型
     */
    private String cycleType;

    /**
     * 描述
     */
    private String description;

    PlanCycleTypeEnum(String cycleType, String description) {
        this.cycleType = cycleType;
        this.description = description;
    }

    public String getCycleType() {
        return cycleType;
    }

    public String getDescription() {
        return description;
    }
}