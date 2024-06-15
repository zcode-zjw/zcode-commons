package com.zcode.zjw.common.file.xml.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author zhangjiwei
 * @since 2022年02月26日
 */
public class CommonUtils {
    private static final Logger log = LoggerFactory.getLogger(CommonUtils.class);

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() <= 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static synchronized String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * map转bean
     *
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<String, ?> map, Class<T> clazz) {
        try {
            T obj = clazz.newInstance();
            Field field;
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                field = clazz.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(obj, entry.getValue());
            }
            return obj;
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            log.error("map转bean异常--", e);
        }
        return null;
    }

    public static String arrToStr(Object[] objects) {
        StringBuilder sb = new StringBuilder("[");
        for (Object object : objects) {
            sb.append(object.toString()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }
}
