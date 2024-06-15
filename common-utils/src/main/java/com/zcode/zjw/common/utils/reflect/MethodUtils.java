package com.zcode.zjw.common.utils.reflect;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 方法工具类
 *
 * @author zhangjiwei
 * @since 2023/8/4
 */
public class MethodUtils {

    public static List<Map<String, Object>> findAllFieldInfo(Class<?> clazz) {
        List<Map<String, Object>> res = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            res.add(new HashMap<String, Object>() {{
                put("name", field.getName());
                String type = field.getType().getSimpleName();
                put("type", type);
                if (type.equals("Integer")) {
                    put("jdbcResult", "getInt");
                } else {
                    put("jdbcResult", "get" + type);
                }
            }});
        }

        return res;
    }

    @Data
    static class User {
        private String name;

        private Integer age;
    }

    public static void main(String[] args) {
        System.out.println(findAllFieldInfo(User.class));
    }

}
