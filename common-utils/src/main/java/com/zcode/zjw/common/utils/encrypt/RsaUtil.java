package com.zcode.zjw.common.utils.encrypt;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


/**
 * RSA加解密工具
 *
 * @author zhangjiwei
 * created on 2021/5/27
 */
@Slf4j
public class RsaUtil {

    /**
     * 算法
     */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 类型
     */
    public static final String RSA_TYPE = "RSA/ECB/PKCS1Padding";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";


    /**
     * 生成RSA公钥与私钥
     *
     * @param
     * @return 生成RSA公钥和私钥字符串
     */
    public static Map<String, String> generateSecretKey() {
        Map<String, String> secretKeyMap = new HashMap<>();
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(512, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            //公钥
            String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
            //私钥
            String privateKeyString = new String(Base64.encodeBase64(privateKey.getEncoded()));
            secretKeyMap.put("public", publicKeyString);
            secretKeyMap.put("private", privateKeyString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secretKeyMap;
    }

    /**
     * 获取转换后的公钥
     *
     * @param publicKey 公钥字符串
     * @return 转换后的公钥
     */
    private static PublicKey getPublicKey(String publicKey) {
        try {
            byte[] byteKey = Base64.decodeBase64(publicKey);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取转换后的私钥
     *
     * @param privateKey 私钥字符串
     * @return 转换后的私钥
     */
    private static PrivateKey getPrivateKey(String privateKey) {
        try {
            byte[] byteKey = Base64.decodeBase64(privateKey);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥签名
     *
     * @param privateKey 私钥
     * @param plainText  明文
     * @return 签名字符串
     */
    public static String sign(String privateKey, String plainText) {
        String signStr = null;
        byte[] signeBytes;
        try {
            log.info("签名前明文:{}", plainText);
            PrivateKey key = getPrivateKey(privateKey);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(key);
            signature.update(plainText.getBytes());
            signeBytes = signature.sign();
            signStr = Base64.encodeBase64String(signeBytes);
            log.info("签名后密文:{}", signStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signStr;
    }

    /**
     * 公钥验签
     *
     * @param plainText 待验签明文
     * @param signStr   签名密文
     * @return true/false
     */
    public static boolean verifySign(String publicKey, String plainText, String signStr) {
        boolean verifySignSuccess = false;
        try {
            PublicKey key = getPublicKey(publicKey);
            Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
            verifySign.initVerify(key);
            verifySign.update(plainText.getBytes());
            verifySignSuccess = verifySign.verify(Base64.decodeBase64(signStr));
            log.info("公钥验签结果:{}", verifySignSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verifySignSuccess;
    }

    /**
     * 明文加密
     *
     * @param publicKey 公钥
     * @param plainText 明文
     * @return 密文
     */
    public static String encrypt(String publicKey, String plainText) {
        String encryptedBase64 = "";
        try {
            Key key = getPublicKey(publicKey);
            final Cipher cipher = Cipher.getInstance(RSA_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 转换
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            encryptedBase64 = Base64.encodeBase64String(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedBase64;
    }

    /**
     * 密文解密
     *
     * @param privateKey      秘钥
     * @param encryptedBase64 密文
     * @return 明文
     */
    public static String decrypt(String privateKey, String encryptedBase64) {
        String decryptedString = "";
        try {
            Key key = getPrivateKey(privateKey);
            final Cipher cipher = Cipher.getInstance(RSA_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedBytes = Base64.decodeBase64(encryptedBase64);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            decryptedString = new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedString;
    }

    public static String decryptStr(String publicKey, String privateKey, String encryptStr) {
        RSA rsa = new RSA(privateKey, publicKey);
        return rsa.decryptStr(encryptStr, KeyType.PrivateKey);
    }

    public static void main(String[] args) {
        Map<String, String> secretKey = RsaUtil.generateSecretKey();
        // 公钥
        String publicKey = secretKey.get("public");
        System.out.println("公钥: " + publicKey);
        // 私钥
        String privateKey = secretKey.get("private");
        System.out.println("私钥: " + privateKey);
        // 明文内容
        String content = "{\n" +
                "    \"params\": {\n" +
                "        \"queries\": {\n" +
                "            \"username\": \"zjw\"\n" +
                "        },\n" +
                "        \"fuzzyQueries\": {\n" +
                "            \"username\": \"z\"\n" +
                "        },\n" +
                "        \"condition\": {\n" +
                "            \"id\": 1\n" +
                "        },\n" +
                "        \"pagination\": {\n" +
                "            \"page\": 1,\n" +
                "            \"size\": 10\n" +
                "        },\n" +
                "        \"sortField\": \"id\",\n" +
                "        \"sortType\": 1\n" +
                "    }\n" +
                "}";
        // 使用公钥,私钥初始化RSA对象
        RSA rsa = new RSA(privateKey, publicKey);
        // 公钥加密
        String encryptStr = rsa.encryptBase64(content, KeyType.PublicKey);
        System.out.println("加密后: " + encryptStr);
        // 私钥解密
        String plainText = rsa.decryptStr(encryptStr, KeyType.PrivateKey);
        System.out.println("解密后: " + plainText);
        System.out.println(rsa.getPublicKey());
        System.out.println(rsa.getPublicKeyBase64());
    }

}