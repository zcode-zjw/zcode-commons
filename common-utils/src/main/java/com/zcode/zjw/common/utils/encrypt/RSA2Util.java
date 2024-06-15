package com.zcode.zjw.common.utils.encrypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

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
 * @author zhangjiwei
 * @description RSA工具类2
 * @date 2023/2/10 下午9:31
 */
public class RSA2Util {

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = decryptBASE64(publicKey);
        Security.addProvider(new BouncyCastleProvider());
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return encryptBASE64(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = decryptBASE64(str);
        //base64编码的私钥
        byte[] decoded = decryptBASE64(privateKey);
        Security.addProvider(new BouncyCastleProvider());
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }

    //编码返回字符串
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    //解码返回byte
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * 密钥长度 于原文长度对应 以及越长速度越慢
     */
    private final static int KEY_SIZE = 1024;
    /**
     * 用于封装随机产生的公钥与私钥
     */
    private static Map<Integer, String> keyMap = new HashMap<>();

    /**
     * 随机生成密钥对
     *
     * @throws Exception
     */
    public static Map<Integer, String> genKeyPair() throws Exception {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器
        keyPairGen.initialize(KEY_SIZE, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = encryptBASE64(publicKey.getEncoded());
        // 得到私钥字符串
        String privateKeyString = encryptBASE64(privateKey.getEncoded());
        // 将公钥和私钥保存到Map
        //0表示公钥
        keyMap.put(0, publicKeyString);
        //1表示私钥
        keyMap.put(1, privateKeyString);

        return keyMap;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(genKeyPair());
        System.out.println(encrypt("{\n" +
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
                "}", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIKFZ7Fnb6WeldYy8Tlz3cZ/peouM8Ncxj/rSs\n" +
                "ux3j06drcZQeZmcKFXXbYRZ46YwkY2E43oE2selGVT0/FVdkRNHmbFRGWWIPIPzGlJkWXmH7511v\n" +
                "6J/kuGnitom1f/KGg0g0QKS1bO6eqees0SdtcQVKgP7qOZBMUJ0cKPQQTQIDAQAB"));
    }

}
