package com.zcode.zjw.timejob.common;

/**
 * @author zhangjiwei
 * @description 任务时间单位
 * @date 2022/11/4 下午9:27
 */
public enum JobTimeUnitEnum {

    SECOND(1, "秒"),
    MINUTE(60, "分"),
    HOUR(3600, "时"),
    DAY(24 * 3600, "天"),
    MONTH(24 * 3600 * 30, "月"),
    YEAR(24 * 3600 * 30 * 12, "年");

    JobTimeUnitEnum(int secondNum, String desc) {
        this.secondNum = secondNum;
        this.desc = desc;
    }

    /**
     * 换算为秒数的数值
     */
    private int secondNum;

    /**
     * 中文描述
     */
    private String desc;

    public int getSecondNum() {
        return secondNum;
    }

    public String getDesc() {
        return desc;
    }

    public static JobTimeUnitEnum selectType(String desc) {
        for (JobTimeUnitEnum value : JobTimeUnitEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return null;
    }

}
