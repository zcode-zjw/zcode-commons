package com.zcode.zjw.web.encrypt.interception;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.zcode.zjw.web.encrypt.annotation.ApiDecrypt;
import com.zcode.zjw.web.encrypt.common.EncryptAlgorithmEnum;
import com.zcode.zjw.web.encrypt.common.EncryptKeyLoader;
import com.zcode.zjw.web.encrypt.filter.ReqParamWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 接口解密拦截器
 *
 * @author zhangjiwei
 * @since 2021/03/19 10:
 **/
@Slf4j
public class DecryptInterceptor implements HandlerInterceptor {

    @Autowired
    private EncryptKeyLoader encryptKeyLoader;

    @Value("${zcode.web.api-decrypt.base-package-path:null}")
    private String basePackageScanPath;

    @Value("${zcode.web.api-encrypt.algorithm:rsa}")
    private String encryptAlgorithm;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        try {
            if (handler instanceof HandlerMethod) {
                ReqParamWrapper requestParameterWrapper = new ReqParamWrapper(request);
                /* 把handler强转为HandlerMethod */
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                // 注解是否加在类上
                ApiDecrypt apiDecryptForClass = handlerMethod.getBeanType().getAnnotation(ApiDecrypt.class);
                /* 从handlerMethod中获取本次请求的接口方法对象然后判断该方法上是否标有我们自定义的注解@ApiDecrypt */
                ApiDecrypt apiDecryptForMethod = handlerMethod.getMethod().getAnnotation(ApiDecrypt.class);
                // 优先处理在方法上和类上标记了的注解
                if (apiDecryptForMethod != null || apiDecryptForClass != null) {
                    if(decryptDeal(Optional.ofNullable(apiDecryptForMethod).orElse(apiDecryptForClass), handlerMethod, request, requestParameterWrapper)) {
                        request = requestParameterWrapper;
                    }
                }

                /* 如果配置了全局或扫描路径 */
                basePackageScanPath = basePackageScanPath.trim();
                if (!"null".equals(basePackageScanPath)) {
                    ApiDecrypt templateAnnotation = ApiDecryptTemplate.class.getDeclaredMethod("templateMethod").getAnnotation(ApiDecrypt.class);
                    // 1、对于根目录就设置为*的
                    if ("*".equals(basePackageScanPath)) {
                        if(decryptDeal(templateAnnotation, handlerMethod, request, requestParameterWrapper)) {
                            request = requestParameterWrapper;
                        }
                    }
                    // 2、对于设置了扫描路径的情况（不包含有通配符的情况）
                    if (basePackageScanPath.contains("*")) {
                        throw new Exception("扫描路径错误，暂不支持通配符 '*'");
                    }
                    String currentMethodPackagePath = handlerMethod.getBeanType().getPackage().getName();
                    if (scanPathContains(currentMethodPackagePath, basePackageScanPath)) {
                        if(decryptDeal(templateAnnotation, handlerMethod, request, requestParameterWrapper)) {
                            request = requestParameterWrapper;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }

        return true;
    }

    private boolean decryptDeal(ApiDecrypt annotation, HandlerMethod handlerMethod,
                                HttpServletRequest request, ReqParamWrapper requestParameterWrapper) {
        if (request.getParameterMap().size() > 0) {
            // 优先使用代码打上注解，随后是配置文件设置的
            EncryptAlgorithmEnum encryptAlgorithmEnum = EncryptAlgorithmEnum.selectEncryptAlgorithm(
                    Optional.ofNullable(annotation.algorithm().getKey()).orElse(encryptAlgorithm));
            if (encryptAlgorithmEnum != null) {
                if (encryptAlgorithmEnum.equals(EncryptAlgorithmEnum.RSA)) {
                    // 获取请求参数
                    Map<Object, Object> map = new HashMap<>();
                    // 转换类型
                    request.getParameterMap().forEach(map::put);
                    // 递归解密
                    recurDecryptMap(map, encryptKeyLoader.getRsaObj(), request, requestParameterWrapper);
                }
            }
        }
        return true;
    }

    /**
     * 类包路径是否包含于扫描包路径
     *
     * @return 是/否
     */
    private boolean scanPathContains(String currentClassPackagePath, String basePackageScanPath) {
        // 如果当前类的包路径都不在扫描包路径，直接返回false，如果当前类的包路径长度小于扫描包路径，返回false
        if (basePackageScanPath.contains(currentClassPackagePath)
                || basePackageScanPath.length() > currentClassPackagePath.length()) {
            return false;
        }
        // 按.分割开，从第一个开始比较
        String[] basePackageScanPathArray = basePackageScanPath.split("\\.");
        String[] currentClassPackagePathArray = currentClassPackagePath.split("\\.");
        int iterNum = Math.min(basePackageScanPathArray.length, currentClassPackagePathArray.length);
        for (int i = 0; i < iterNum; i++) {
            // 如果对应位置的值不同，返回false
            if (!basePackageScanPathArray[i].equals(currentClassPackagePathArray[i])) {
                return false;
            }
        }
        return true;
    }

    static class ApiDecryptTemplate {

        @ApiDecrypt
        public void templateMethod() {
        }

    }

    /**
     * 递归解密map类型文本数据
     *
     * @param map 操作map
     * @param rsa RSA对象
     */
    private static void recurDecryptMap(Map<Object, Object> map, RSA rsa, HttpServletRequest request, ReqParamWrapper requestParameterWrapper) {
        for (Object key : map.keySet()) {
            if (map.get(key) instanceof Map) {
                recurDecryptMap((Map<Object, Object>) map.get(key), rsa, request, requestParameterWrapper);
            } else if (map.get(key) instanceof String[]) {
                String string = ((String[]) map.get(key))[0];
                String decryptStr = rsa.decryptStr(string.replace(" ", "+"), KeyType.PrivateKey);
                if (request.getParameter(key + "") != null) {
                    requestParameterWrapper.addParameter(key + "", decryptStr);
                }
                map.put(key, decryptStr);
            }
        }
    }

}
