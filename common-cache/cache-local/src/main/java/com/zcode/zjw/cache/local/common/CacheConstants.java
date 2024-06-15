package com.zcode.zjw.cache.local.common;

public class CacheConstants {
    /**
     * 默认过期时间（配置类中我使用的时间单位是秒，所以这里如 3*60 为3分钟）
     */
    public static final int DEFAULT_EXPIRES = 3 * 60;
    public static final int EXPIRES_5_MIN = 5 * 60;
    public static final int EXPIRES_10_MIN = 10 * 60;

    public static final String GET_USER = "GET:USER";
    public static final String GET_DYNAMIC = "GET:DYNAMIC";

    public static final String FIRST_CACHE = "FIRST_CACHE";
    public static final String SECOND_CACHE = "SECOND_CACHE";
    public static final String THREE_CACHE = "THREE_CACHE";

}