package com.zcode.zjw.schedule.quartz.util;

import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RSA加密 原理：密文＝明文^E mod N 明文＝密文^D mod N
 *
 * @author zhangjiwei
 * @since 2022年03月23日
 */
public class RSAUtil {
    private static final Logger logger = LoggerFactory.getLogger(RSAUtil.class);
    // N
    private static String modulus =
            "120749774428185480467622030722535804071445078993623309709775427878906293937338059423076695854937532244466465395164541641368876529295825453848760144835049363522545908104302024165873971414491110512342791594610742544380402908598585190494003507524195754273822268813447403290817343077571516216147839402414940310061";
    // E
    private static String public_exponent = "65537";
    // D
    private static String private_exponent =
            "73923469942286919561803730971048133587965873619209827001168953680477872428610977313161774128992838682156392947263251899461404460204267953359475632559803617319478756560848229397545070273747796303141458040475889804016062973476403760709402857872540300632704514872361476749953797952016730690123983122643596231873";

    /**
     * 使用默认公钥加密,生成256位的密文,由0-F组成
     *
     * @param data 明文
     * @return 密文
     * @throws Exception
     */
    public static String encryptByPublicKey(String data) throws Exception {
        RSAPublicKey publicKey = getPublicKey(modulus, public_exponent);
        return encryptByPublicKey(data, publicKey);
    }

    /**
     * 使用指定公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey) throws Exception {
        if (publicKey == null) {
            return null;
        } else {
            Cipher cipher = Cipher.getInstance("RSA");
            // 设置编码方式和秘钥
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int key_len = publicKey.getModulus().bitLength() / 8;
            // 每次加密的字节数，不能超过密钥的长度值减去11
            String[] ss = splitString(data, key_len - 11);
            StringBuilder sb = new StringBuilder();
            for (String s : ss) {
                sb.append(bcdToStr(cipher.doFinal(s.getBytes())));
            }
            return sb.toString();
        }
    }

    /**
     * 使用默认私钥解密
     *
     * @param data 密文
     * @return 明文
     * @throws Exception
     */
    public static String dencryptByPrivateKey(String data) throws Exception {
        RSAPrivateKey privateKey = getPrivateKey(modulus, private_exponent);
        return dencryptByPrivateKey(data, privateKey);
    }

    /**
     * 使用指定私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String dencryptByPrivateKey(String data, RSAPrivateKey privateKey)
            throws Exception {
        if (privateKey == null) {
            return "";
        }
        Cipher cipher = Cipher.getInstance("RSA");
        // 设置编码方式和秘钥
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int key_len = privateKey.getModulus().bitLength() / 8;
        byte[] bytes = data.getBytes();
        byte[] bcds = ascToBcds(bytes, bytes.length);
        byte[][] arrays = splitArray(bcds, key_len);
        StringBuilder sb = new StringBuilder();
        for (byte[] bytes1 : arrays) {
            sb.append(new String(cipher.doFinal(bytes1)));
        }
        return sb.toString();
    }

    /**
     * 使用默认N、E还原公钥
     *
     * @return
     */
    public static RSAPublicKey getPublicKey() {
        return getPublicKey(modulus, public_exponent);
    }

    /**
     * 使用N、E还原公钥
     *
     * @param modulus
     * @param exponent
     * @return
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        BigInteger b1 = new BigInteger(modulus);
        BigInteger b2 = new BigInteger(exponent);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 使用默认N、D还原私钥
     *
     * @return
     */
    public static RSAPrivateKey getPrivateKey() {
        return getPrivateKey(modulus, private_exponent);
    }

    /**
     * 使用N、D还原私钥
     *
     * @param modulus
     * @param exponent
     * @return
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        BigInteger b1 = new BigInteger(modulus);
        BigInteger b2 = new BigInteger(exponent);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 获取的RSA加密的公私钥对,publicKey：公钥，privateKey：私钥 每次产生的publicKey和privateKey都是不同的一对
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Map<String, Object> getKeys() {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        PrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> map = new HashMap<>();
        map.put("publicKey", publicKey);
        map.put("privateKey", privateKey);
        return map;
    }

    public static byte[] ascToBcds(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; ++i) {
            bcd[i] = ascToBcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len ? 0 : ascToBcd(ascii[j++])) & 255) + (bcd[i] << 4));
        }
        return bcd;
    }

    /**
     * ascll码转bcd码
     *
     * @param asc
     * @return
     */
    public static byte ascToBcd(byte asc) {
        byte bcd;
        if (asc >= 48 && asc <= 57) {
            // 0-9
            bcd = (byte) (asc - 48);
        } else if (asc >= 65 && asc <= 70) {
            // A-F
            bcd = (byte) (asc - 65 + 10);
        } else if (asc >= 97 && asc <= 102) {
            // a-f
            bcd = (byte) (asc - 97 + 10);
        } else {
            bcd = (byte) (asc - 48);
        }
        return bcd;
    }

    public static String bcdToStr(byte[] bytes) {
        char[] temp = new char[bytes.length * 2];

        for (int i = 0; i < bytes.length; ++i) {
            char val = (char) ((bytes[i] & 240) >> 4 & 15);
            temp[i * 2] = (char) (val > '\t' ? val + 65 - 10 : val + 48);
            val = (char) (bytes[i] & 15);
            temp[i * 2 + 1] = (char) (val > '\t' ? val + 65 - 10 : val + 48);
        }

        return new String(temp);
    }

    /**
     * 对字符串加密，RSA最大加密明文大小len为117
     *
     * @param s
     * @param len
     * @return
     */
    public static String[] splitString(String s, int len) {
        int x = s.length() / len;
        int y = s.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }

        String[] ss = new String[x + z];
        String str = "";
        for (int i = 0; i < x + z; ++i) {
            if (i == x + z - 1 && y != 0) {
                str = s.substring(i * len, i * len + y);
            } else {
                str = s.substring(i * len, i * len + len);
            }
            ss[i] = str;
        }
        return ss;
    }

    public static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }

        byte[][] arrays = new byte[x + z][];

        for (int i = 0; i < x + z; ++i) {
            byte[] arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                // 复制数组
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }

}
