package com.zcode.zjw.web.encrypt.advice;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.zcode.zjw.common.utils.encrypt.RsaUtil;
import com.zcode.zjw.common.utils.reflect.ReflectionUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author zhangjiwei
 * @description 接口访问时数据解密
 * @date 2023/2/9 下午3:55
 */
@ControllerAdvice
public class DecryptRequestAdvice extends RequestBodyAdviceAdapter {

    @Value("${zcode.web.api-encrypt.open:false}")
    private Boolean open;

    @Value("${zcode.web.api-encrypt.algorithm:rsa}")
    private String encryptAlgorithm;

    @Override
    public boolean supports(@NotNull MethodParameter methodParameter, @NotNull Type type,
                            @NotNull Class<? extends HttpMessageConverter<?>> aClass) {
        return open != null && open;
    }

    @NotNull
    @Override
    public Object afterBodyRead(@NotNull Object body, @NotNull HttpInputMessage inputMessage, @NotNull MethodParameter parameter,
                                @NotNull Type targetType, @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            recurDecryptMap((Map<Object, Object>) body, getRsaObj());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

    private RSA getRsaObj() {
        // 对数据解密
        Map<String, String> secretKey = RsaUtil.generateSecretKey();
        // 公钥
        String publicKey = secretKey.get("public");
        // 私钥
        String privateKey = secretKey.get("private");
        // 使用公钥,私钥初始化RSA对象
        return new RSA(privateKey, publicKey);
    }

    private static void recurDecryptBody(Map<String, Object> objectMap) {
        // 对数据解密
        Map<String, String> secretKey = RsaUtil.generateSecretKey();
        // 公钥
        String publicKey = secretKey.get("public");
        // 私钥
        String privateKey = secretKey.get("private");
        // 使用公钥,私钥初始化RSA对象
        RSA rsa = new RSA(privateKey, publicKey);
        // 获取所有value
        for (String key : objectMap.keySet()) {
            // 解密字符串类型的数据
            if (objectMap.get(key) instanceof String) {
                objectMap.put(key, rsa.decryptStr((String) objectMap.get(key), KeyType.PrivateKey));
            }
        }
    }

    public static void recurDecryptObj(Object object, RSA rsa) {
        // 使用公钥,私钥初始化RSA对象
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        // 如果本身就是
        for (Field field : fields) {
            //设置属性是可以访问的(私有的也可以)
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(object);
                // 字段值为空、类型不为字符串时不处理
                if (value != null && !"serialVersionUID".equals(field.getName())) {
                    if (value instanceof String) {
                        String decryptStr = rsa.decryptStr(String.valueOf(value), KeyType.PrivateKey);
                        ReflectionUtil.setFieldValue(object, field.getName(), decryptStr);
                    } else {
                        // 判断值是否是对象
                        System.out.println(value.toString());
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void recurDecryptMap(Map<Object, Object> map, RSA rsa) {
        for (Object key : map.keySet()) {
            if (map.get(key) instanceof Map) {
                recurDecryptMap((Map<Object, Object>) map.get(key), rsa);
            }
            else if (map.get(key) instanceof String) {
                String decryptStr = rsa.decryptStr(String.valueOf(map.get(key)), KeyType.PrivateKey);
                map.put(key, decryptStr);
            }
        }
    }

//    public static void main(String[] args) throws IllegalAccessException {
//        Map<String, String> secretKey = RsaUtil.generateSecretKey();
//        // 公钥
//        String publicKey = secretKey.get("public");
//        // 私钥
//        String privateKey = secretKey.get("private");
//        // 使用公钥,私钥初始化RSA对象
//        RSA rsa = new RSA(privateKey, publicKey);
//
//        Map<Object, Object> map = new HashMap<>();
//        map.put("name", rsa.encryptBase64("gaojie", KeyType.PublicKey));
//        Map<Object, Object> innerMap = new HashMap<>();
//        map.put("children-1", innerMap);
//        innerMap.put("name", rsa.encryptBase64("gaojie", KeyType.PublicKey));
//        innerMap.put("age", 18);
//        System.out.println(map);
//        recurDecryptMap(map, rsa);
//        System.out.println(map);
//
////        GhdqsOampUser ghdqsOampUser = new GhdqsOampUser();
////        ghdqsOampUser.setUserName(rsa.encryptBase64("zjw", KeyType.PublicKey));
////        ghdqsOampUser.setId(2);
////        ghdqsOampUser.setPassword(rsa.encryptBase64("password", KeyType.PublicKey));
////        Test test = new Test();
////        test.setUser(ghdqsOampUser);
////        test.setName(rsa.encryptBase64("zjw", KeyType.PublicKey));
////        test.setAge(18);
////        test.setSex(true);
////        System.out.println(test);
////        recurDecryptObj(map, rsa);
//    }
//
//    @Data
//    static class Test {
//
//        private GhdqsOampUser user;
//
//        private String name;
//
//        private Boolean sex;
//
//        private Integer age;
//
//    }

}
