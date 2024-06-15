package com.zcode.zjw.common.web.query.enhancer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.zcode.zjw.common.utils.encrypt.AesUtil;
import com.zcode.zjw.common.utils.encrypt.DesUtil;
import com.zcode.zjw.common.utils.encrypt.RsaUtil;
import com.zcode.zjw.common.web.query.annotation.QueryParser;
import com.zcode.zjw.common.web.query.common.AggregateQuerySecretKeyConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述
 *
 * @author zhangjiwei
 * @since 2023/8/11
 */
@Component
public class QueryParameterParseProcessor implements HandlerMethodArgumentResolver {

    @Value("${query.aggregate.encrypt.open:false}")
    private boolean queryEncrypt;

    @Value("${query.aggregate.encrypt.way:rsa}")
    private String queryEncryptWay;

    @Value("${query.aggregate.encrypt.privateKey:null}")
    private String privateKey;

    @Value("${query.aggregate.encrypt.ase.iv:null}")
    private String aseIv;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(QueryParser.class);
    }

    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) (webRequest.getNativeRequest());
        BufferedReader br = request.getReader();
        String str;
        StringBuilder reqStr = new StringBuilder();
        while ((str = br.readLine()) != null) {
            reqStr.append(str);
        }
        if (queryEncrypt) { // 如果需要加密
            String iv = null;
            if (queryEncryptWay.equals(EncryptStrategy.AES.getKey())) { // AES需要传入IV
                iv = aseIv;
            }
            String decryptStr = EncryptStrategy.decrypt(queryEncryptWay, reqStr.toString(), privateKey, iv);
            reqStr = new StringBuilder(decryptStr);
        }

        Map<String, Object> res = new HashMap<>();
        QueryParser parameterAnnotation = parameter.getParameterAnnotation(QueryParser.class);
        res.put("aggregateQueryParams", JSONPath.read(reqStr.toString(), "$." + parameterAnnotation.value()));
        Map<String, Object> params = JSONObject.parseObject(reqStr.toString()).toJavaObject(Map.class);
        params.remove(parameterAnnotation.value());
        res.put("commonParams", params);

        return new JSONObject(res);
    }

    @AllArgsConstructor
    @Getter
    enum EncryptStrategy {
        RSA("rsa", "RSA加密") {
            @Override
            protected String decryptLogic(String encryptedString, String privateKey, Object... args) throws Exception {
                if (privateKey.equals("null")) {
                    privateKey = AggregateQuerySecretKeyConstant.rsaPrivateKey;
                }
                return RsaUtil.decryptStr(AggregateQuerySecretKeyConstant.rsaPublicKey, privateKey, encryptedString);
            }
        },
        AES("aes", "AES加密") {
            @Override
            protected String decryptLogic(String encryptedString, String privateKey, Object... args) throws Exception {
                if (privateKey.equals("null")) {
                    privateKey = AggregateQuerySecretKeyConstant.aesPrivateKey;
                }
                String iv = (String) args[0];
                if (iv.equals("null")) {
                    iv = AggregateQuerySecretKeyConstant.aesIv;
                }

                return AesUtil.decrypt(encryptedString, privateKey, iv);
            }
        },
        DES("des", "DES加密") {
            @Override
            protected String decryptLogic(String encryptedString, String privateKey, Object... args) throws Exception {
                if (privateKey.equals("null")) {
                    privateKey = AggregateQuerySecretKeyConstant.desPrivateKey;
                }
                return new String(DesUtil.decrypt(encryptedString.getBytes(), privateKey));
            }
        },
        ;

        private final String key;

        private final String desc;

        protected abstract String decryptLogic(String encryptedString, String privateKey, Object... args) throws Exception;

        public static String decrypt(String key, String encryptedString, String privateKey, Object... args) throws Exception {
            for (EncryptStrategy strategy : EncryptStrategy.values()) {
                if (strategy.getKey().equals(key)) {
                    return strategy.decryptLogic(encryptedString, privateKey, args);
                }
            }

            throw new RuntimeException("找不到加密策略（聚合查询）: " + key);
        }
    }

}
