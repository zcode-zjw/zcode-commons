package com.zcode.zjw.web.encrypt.strategy;

import com.zcode.zjw.common.utils.reflect.DynamicEnumUtils;
import com.zcode.zjw.web.encrypt.common.EncryptAlgorithmEnum;
import com.zcode.zjw.web.encrypt.filter.ReqParamWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangjiwei
 * @description 加解密算法策略接口
 * @date 2023/2/10 下午5:20
 */
public interface EncryptAlgorithmStrategy {

    /**
     * 外部调用解密接口
     *
     * @param request                 原始请求
     * @param requestParameterWrapper 后续请求
     */
    default void decrypt(HttpServletRequest request, ReqParamWrapper requestParameterWrapper) throws Exception {
        if (request.getParameterMap().size() > 0) {
            // 获取请求参数
            Map<Object, Object> map = new HashMap<>();
            // 转换类型
            request.getParameterMap().forEach(map::put);
            // 解密（递归所有层级数据）
            recurDecryptMap(map, request, requestParameterWrapper);
        }
    }

    /**
     * 内部具体实现的解密处理
     *
     * @param encryptStr 加密字符串
     */
    String decryptStr(String encryptStr) throws Exception;

    /**
     * 递归解密map类型文本数据
     *
     * @param map 操作map
     */
    default void recurDecryptMap(Map<Object, Object> map, HttpServletRequest request, ReqParamWrapper requestParameterWrapper) throws Exception {
        for (Object key : map.keySet()) {
            if (map.get(key) instanceof Map) {
                recurDecryptMap((Map<Object, Object>) map.get(key), request, requestParameterWrapper);
            } else if (map.get(key) instanceof String[]) {
                String string = ((String[]) map.get(key))[0];
                // 可能是转类型造成字符串+号变成了空格，这里手动替换一下
                string = string.replace(" ", "+");
                // 解密
                String decryptStr = decryptStr(string);
                if (request.getParameter(key + "") != null) {
                    requestParameterWrapper.addParameter(key + "", decryptStr);
                }
                map.put(key, decryptStr);
            }
        }
    }


    default void strategyRegister(String name, String key, String desc) {
        if (EncryptAlgorithmEnum.selectEncryptAlgorithm(key) == null) {
            DynamicEnumUtils.addEnum(EncryptAlgorithmEnum.class, name,
                    new Class[]{key.getClass(), desc.getClass()}, new String[]{key, desc});
        }
    }

    void register();

    boolean supports(String encryptAlgorithm);

}
