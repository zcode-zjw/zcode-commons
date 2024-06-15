package com.zcode.zjw.auth.common;

import lombok.Getter;

/**
 * 认证信息存储类型
 *
 * @author zhangjiwei
 * @date 2023/7/15
 */
@Getter
public enum AuthInfoStoreTypeEnum {

    LOCAL("local", "本地Session存储"),
    CACHE("cache", "远程缓存存储"),
    TOKEN("token", "远程缓存 + Token方式存储");

    private String key;

    private String desc;

    AuthInfoStoreTypeEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

}
