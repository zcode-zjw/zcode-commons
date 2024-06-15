package com.zcode.zjw.web.limiter.common;

/**
 * 限流类型
 *
 * @author zhangjiwei
 */

public enum LimitType {

    /**
     * 默认策略全局限流
     */
    DEFAULT("default"),

    /**
     * 根据请求者IP进行限流
     */
    IP("ip"),

    /**
     * 根据请求者UserId进行限流
     */
    USER("userid");

    private String key;

    LimitType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static LimitType selectLimitType(String key) {
        for (LimitType value : LimitType.values()) {
            if (value.getKey().equals(key)) {
                return value;
            }
        }
        return null;
    }

}
