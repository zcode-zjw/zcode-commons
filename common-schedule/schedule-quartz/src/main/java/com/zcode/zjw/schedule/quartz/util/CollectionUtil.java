package com.zcode.zjw.schedule.quartz.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author zhangjiwei
 * @since 2022年03月20日
 */
public class CollectionUtil {

    public static boolean isBlank(Object[] objects) {
        return objects == null || objects.length <= 0;
    }

    public static boolean isBlank(Collection<Object> collection) {
        return collection == null || collection.size() <= 0;
    }

    public static boolean isBlank(Map<Object, Object> map) {
        return map == null || map.size() <= 0;
    }

    public static boolean isBlank(Object object) {
        if (object instanceof Collection) {
            Collection<Object> objects = (Collection<Object>) object;
            return isBlank(objects);
        } else if (object instanceof Map) {
            Map<Object, Object> map = (Map<Object, Object>) object;
            return isBlank(map);
        } else {
            return object == null || "".equals(object);
        }
    }

    public static boolean isNotBlank(Object[] objects) {
        return !isBlank(objects);
    }

    public static boolean isNotBlank(Collection<Object> collection) {
        return !isBlank(collection);
    }

    public static boolean isNotBlank(Map<Object, Object> map) {
        return !isBlank(map);
    }

    public static boolean isNotBlank(Object object) {
        if (object instanceof Collection) {
            Collection<Object> objects = (Collection<Object>) object;
            return isNotBlank(objects);
        } else if (object instanceof Map) {
            Map<Object, Object> map = (Map<Object, Object>) object;
            return isNotBlank(map);
        } else {
            return object != null && !"".equals(object);
        }
    }

    /**
     * 转换list为数组，list元素类型必须统一
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> T[] toArray(List<T> list) {
        if (isBlank(list)) {
            return null;
        }
        // Array.newInstance新建一个指定类型和长度的数组
        T[] ts = (T[]) Array.newInstance(list.get(0).getClass(), list.size());
        list.toArray(ts);
        return ts;
    }
}
