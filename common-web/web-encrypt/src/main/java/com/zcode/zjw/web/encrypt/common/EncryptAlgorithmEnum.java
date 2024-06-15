package com.zcode.zjw.web.encrypt.common;


/**
 * @author zhangjiwei
 * @description 加密解密算法
 * @date 2023/2/10 上午9:59
 */
public enum EncryptAlgorithmEnum {

    RSA("rsa", "RSA算法"),

    DES("des", "DES算法"),

    AES("aes", "AES算法");

    private String key;

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    private String desc;

    EncryptAlgorithmEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static EncryptAlgorithmEnum selectEncryptAlgorithm(String key) {
        for (EncryptAlgorithmEnum value : EncryptAlgorithmEnum.values()) {
            if (value.key.equals(key)) {
                return value;
            }
        }
        return null;
    }

}
