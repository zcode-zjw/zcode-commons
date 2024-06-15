package com.zcode.zjw.web.encrypt.strategy.impl;

import com.zcode.zjw.common.utils.encrypt.AesUtil;
import com.zcode.zjw.web.encrypt.common.EncryptAlgorithmEnum;
import com.zcode.zjw.web.encrypt.common.EncryptKeyLoader;
import com.zcode.zjw.web.encrypt.strategy.EncryptAlgorithmStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangjiwei
 * @description AES算法策略
 * @date 2023/2/10 下午5:24
 */
@Component
public class EncryptAlgorithmStrategyAes implements EncryptAlgorithmStrategy {

    @Autowired
    private EncryptKeyLoader encryptKeyLoader;

    @Override
    public String decryptStr(String encryptStr) throws Exception {
        return AesUtil.decrypt(encryptStr, encryptKeyLoader.getAesPrivateKey(), encryptKeyLoader.getAesPrivateKey());
    }

    @Override
    public void register() {
        strategyRegister("AES", "aes", "AES加密算法");
    }

    @Override
    public boolean supports(String encryptAlgorithm) {
        return EncryptAlgorithmEnum.AES.equals(EncryptAlgorithmEnum.selectEncryptAlgorithm(encryptAlgorithm));
    }

}
