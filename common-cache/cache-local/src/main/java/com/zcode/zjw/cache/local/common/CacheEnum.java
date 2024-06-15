package com.zcode.zjw.cache.local.common;

public enum CacheEnum {
    /**
     * @date 16:34 2020/10/27
     * 第一个cache
     **/
    FIRST_CACHE("FIRST_CACHE", 300, 20000, 300),
    /**
     * @date 16:35 2020/10/27
     * 第二个cache
     **/
    SECOND_CACHE("FIRST_CACHE", 60, 10000, 200),
    /**
     * @date 16:35 2020/10/27
     * 第三个cache
     **/
    THREE_CACHE("THREE_CACHE", 10, 500, 100);

    private final String name;
    private int second;
    private long maxSize;
    private int initSize;

    CacheEnum(String name, int second, long maxSize, int initSize) {
        this.name = name;
        this.second = second;
        this.maxSize = maxSize;
        this.initSize = initSize;
    }

    public String getName() {
        return name;
    }

    public int getSecond() {
        return second;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public int getInitSize() {
        return initSize;
    }
}