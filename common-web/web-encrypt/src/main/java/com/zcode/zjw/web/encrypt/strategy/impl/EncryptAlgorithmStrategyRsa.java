package com.zcode.zjw.web.encrypt.strategy.impl;

import cn.hutool.crypto.asymmetric.KeyType;
import com.zcode.zjw.web.encrypt.common.EncryptAlgorithmEnum;
import com.zcode.zjw.web.encrypt.common.EncryptKeyLoader;
import com.zcode.zjw.web.encrypt.strategy.EncryptAlgorithmStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangjiwei
 * @description RSA算法策略
 * @date 2023/2/10 下午5:24
 */
@Component
public class EncryptAlgorithmStrategyRsa implements EncryptAlgorithmStrategy {

    @Autowired
    private EncryptKeyLoader encryptKeyLoader;

    @Override
    public String decryptStr(String encryptStr) {
        return encryptKeyLoader.getRsaObj().decryptStr(encryptStr, KeyType.PrivateKey);
    }

    @Override
    public void register() {
        strategyRegister("RSA", "rsa", "RSA加密算法");
    }

    @Override
    public boolean supports(String encryptAlgorithm) {
        return EncryptAlgorithmEnum.RSA.equals(EncryptAlgorithmEnum.selectEncryptAlgorithm(encryptAlgorithm));
    }

}
