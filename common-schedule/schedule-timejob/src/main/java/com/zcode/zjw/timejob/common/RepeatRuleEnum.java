package com.zcode.zjw.timejob.common;

/**
 * @author : zjw
 * @description : 重复规则
 * @since : 2022/8/18 14:11
 */
public enum RepeatRuleEnum {
    WEEK("week", "周"),

    DATE("date", "日期");

    private String value;

    private String description;

    RepeatRuleEnum(String type, String description) {
        this.value = type;
        this.description = description;
    }

    public String getType() {
        return value;
    }

    public void setType(String type) {
        this.value = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}