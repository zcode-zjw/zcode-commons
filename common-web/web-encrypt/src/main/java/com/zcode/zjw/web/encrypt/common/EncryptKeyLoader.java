package com.zcode.zjw.web.encrypt.common;

import cn.hutool.crypto.asymmetric.RSA;
import com.zcode.zjw.common.utils.common.UUIDUtil;
import com.zcode.zjw.common.utils.encrypt.RsaUtil;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author zhangjiwei
 * @description 获取或操作公钥
 * @date 2023/2/10 上午11:41
 */
@Component
@Data
public class EncryptKeyLoader {

    private RSA rsaObj;

    private String aesPrivateKey;

    @PostConstruct
    public void initRsaObj() {
        this.rsaObj = newRsaObj();
        this.aesPrivateKey = UUIDUtil.get16UUID();
    }

    public void refresh() {
        this.rsaObj = newRsaObj();
    }

    private static RSA newRsaObj() {
        // 对数据解密
        Map<String, String> secretKey = RsaUtil.generateSecretKey();
        // 公钥
        String publicKey = secretKey.get("public");
        // 私钥
        String privateKey = secretKey.get("private");
        // 使用公钥,私钥初始化RSA对象
        return new RSA(privateKey, publicKey);
    }

}
