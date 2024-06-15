package com.zcode.zjw.web.encrypt.controller;

import com.zcode.zjw.common.utils.common.UUIDUtil;
import com.zcode.zjw.common.utils.encrypt.RSA2Util;
import com.zcode.zjw.configs.common.Result;
import com.zcode.zjw.log.api.annotation.ApiLog;
import com.zcode.zjw.web.encrypt.common.EncryptKeyLoader;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author zhangjiwei
 * @description 加解密控制器
 * @date 2023/2/9 下午4:13
 */
@RestController
@ApiLog
@Api("加解密控制器")
@RequestMapping("/encrypt/api/")
public class EncryptController {

    @Value("${zcode.web.api-encrypt.des.private-key:null}")
    private String desPrivateKey;

    @Value("${zcode.web.api-encrypt.aes.private-key:null}")
    private String aesPrivateKey;

    @Autowired
    private EncryptKeyLoader encryptKeyLoader;

    /**
     * 获取加密公钥（RSA）
     *
     * @return RSA公钥
     */
    @GetMapping("getPublicKey/rsa")
    @ResponseBody
    public Result<?> getPublicKey() {
        // 获取公钥
        encryptKeyLoader.refresh();
        String publicKey = encryptKeyLoader.getRsaObj().getPublicKeyBase64();
        return Result.success(publicKey);
    }

    /**
     * 获取加密私钥（DES）
     *
     * @return DES私钥
     */
    @PostMapping("getPrivateKey/des")
    @ResponseBody
    public Result<?> getDesPrivateKey(@RequestBody Map<String, Object> params) throws Exception {
        // Des私钥
        String privateKeyForDes;
        if (desPrivateKey == null || "null".equals(desPrivateKey)) {
            privateKeyForDes = "95880288";
        } else {
            privateKeyForDes = desPrivateKey;
        }

        // 获取公钥
        String publicKey = params.get("publicKey") + "";

        // 使用公钥将DES秘钥加密后，返回给前端
        String encryptStr = RSA2Util.encrypt(privateKeyForDes, publicKey);

        return Result.success(encryptStr);

    }

    /**
     * 获取加密私钥（AES）
     *
     * @return AES私钥
     */
    @PostMapping("getPrivateKey/aes")
    @ResponseBody
    public Result<?> getAesPrivateKey(@RequestBody Map<String, Object> params) throws Exception {
        // Aes私钥
        String privateKeyForAes;
        if (aesPrivateKey == null || "null".equals(aesPrivateKey)) {
            privateKeyForAes = UUIDUtil.get16UUID();
            encryptKeyLoader.setAesPrivateKey(privateKeyForAes);
        } else {
            privateKeyForAes = aesPrivateKey;
        }

        // 获取公钥
        String publicKey = params.get("publicKey") + "";

        // 使用公钥将AES秘钥加密后，返回给前端
        String encryptStr = RSA2Util.encrypt(privateKeyForAes, publicKey);

        return Result.success(encryptStr);

    }

}
