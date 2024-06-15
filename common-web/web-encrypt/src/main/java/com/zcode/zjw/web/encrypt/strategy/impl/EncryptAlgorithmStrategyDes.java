package com.zcode.zjw.web.encrypt.strategy.impl;

import com.zcode.zjw.common.utils.encrypt.DesUtil;
import com.zcode.zjw.web.encrypt.common.EncryptAlgorithmEnum;
import com.zcode.zjw.web.encrypt.strategy.EncryptAlgorithmStrategy;
import org.springframework.stereotype.Component;

/**
 * @author zhangjiwei
 * @description DES算法策略
 * @date 2023/2/10 下午5:24
 */
@Component
public class EncryptAlgorithmStrategyDes implements EncryptAlgorithmStrategy {

    @Override
    public String decryptStr(String encryptStr) throws Exception {
        return new String(DesUtil.decrypt(encryptStr.getBytes(), "95880288"));
    }

    @Override
    public void register() {
        strategyRegister("DES", "des", "DES加密算法");
    }

    @Override
    public boolean supports(String encryptAlgorithm) {
        return EncryptAlgorithmEnum.DES.equals(EncryptAlgorithmEnum.selectEncryptAlgorithm(encryptAlgorithm));
    }

}
