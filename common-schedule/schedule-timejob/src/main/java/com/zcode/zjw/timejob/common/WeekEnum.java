package com.zcode.zjw.timejob.common;

/**
 * @author : chenxuebing
 * @description : 星期映射
 * @since : 2022/8/12 10:31
 */
public enum WeekEnum {
    SUNDAY(1, "星期天"),

    MONDAY(2, "星期一"),

    TUESDAY(3, "星期二"),

    WEDNESDAY(4, "星期三"),

    THURSDAY(5, "星期四"),

    FRIDAY(6, "星期五"),

    SATURDAY(7, "星期六");

    private Integer value;

    private String description;

    WeekEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}