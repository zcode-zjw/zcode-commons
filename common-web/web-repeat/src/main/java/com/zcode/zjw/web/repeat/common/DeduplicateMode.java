package com.zcode.zjw.web.repeat.common;

/**
 * @author zhangjiwei
 * @description 防重模式
 * @date 2023/2/8 上午10:48
 */
public enum DeduplicateMode {

    LOCAL_METHOD_TABLE("local_method_table", "本地方法表"),
    CACHE("cache", "缓存"),
    SESSION("session", "Session");

    private String desc;
    private String key;

    DeduplicateMode(String key, String desc) {
        this.desc = desc;
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public String getKey() {
        return key;
    }

    public static DeduplicateMode selectDeduplicateMode(String key) {
        for (DeduplicateMode value : DeduplicateMode.values()) {
            if (value.getKey().equals(key)) {
                return value;
            }
        }
        throw new NotFindDeduplicateModeException();
    }

    static class NotFindDeduplicateModeException extends RuntimeException {
        public NotFindDeduplicateModeException() {
            super("无法找到防重模式");
        }

        public NotFindDeduplicateModeException(String msg) {
            super(msg);
        }
    }

}
