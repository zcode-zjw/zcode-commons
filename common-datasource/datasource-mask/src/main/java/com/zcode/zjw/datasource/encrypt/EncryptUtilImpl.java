package com.zcode.zjw.datasource.encrypt;

import com.zcode.zjw.datasource.annotation.EncryptTransaction;
import com.zcode.zjw.datasource.utils.DBAESUtil;
import com.zcode.zjw.datasource.utils.IEncryptUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author zhangjiwei
 * @description 加密实现类
 * @date 2023/2/13 上午9:10
 */
@Component
public class EncryptUtilImpl implements IEncryptUtil {

    @Override
    public <T> T encrypt(Field[] declaredFields, T paramsObject) throws IllegalAccessException {
            //取出所有被EncryptTransaction注解的字段
        for (Field field : declaredFields) {
            EncryptTransaction encryptTransaction = field.getAnnotation(EncryptTransaction.class);
            if (!Objects.isNull(encryptTransaction)) {
                field.setAccessible(true);
                Object object = field.get(paramsObject);
                //暂时只实现String类型的加密
                if (object instanceof String) {
                    String value = (String) object;
                    //加密
                    try {
                        field.set(paramsObject, DBAESUtil.encrypt(value));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return paramsObject;
    }
}