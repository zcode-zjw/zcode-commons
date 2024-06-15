package com.zcode.zjw.timejob.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangjiwei
 * @since 2021-06-15 10:29
 */
public final class ThreadLocalUtil {

    private ThreadLocalUtil() {
    }

    /**
     * ThreadLocal的静态方法withInitial()会返回一个SuppliedThreadLocal对象
     * 而SuppliedThreadLocal<T> extends ThreadLocal<T>
     * 我们存进去的Map会作为的返回值：
     * 所以也相当于重写了initialValue()
     */
    private final static ThreadLocal<Map<String, Object>> THREAD_CONTEXT = ThreadLocal.withInitial(
            () -> new HashMap<>(8)
    );

    /**
     * 根据key获取value
     * @param key
     * @return
     */
    public static Object get(String key) {
        // getContextMap()表示要先获取THREAD_CONTEXT的value，也就是Map<String, Object>。然后再从Map<String, Object>中根据key获取
        return THREAD_CONTEXT.get().get(key);
    }

    /**
     * put操作，原理同上
     *
     * @param key
     * @param value
     */
    public static void put(String key, Object value) {
        THREAD_CONTEXT.get().put(key, value);
    }

    /**
     * 清除map里的某个值
     * @param key
     * @return
     */
    public static Object remove(String key) {
        return THREAD_CONTEXT.get().remove(key);
    }

    /**
     * 批量清除map里的某个值
     * @param keys
     * @return
     */
    public static void removeByArray(String[] keys) {
        for (String key : keys) {
            THREAD_CONTEXT.get().remove(key);
        }

    }

    /**
     * 清除整个Map<String, Object>
     */
    public static void clear() {
        THREAD_CONTEXT.get().clear();
    }

    /**
     * 从ThreadLocalMap中清除当前ThreadLocal存储的内容
     */
    public static void clearAll() {
        THREAD_CONTEXT.remove();
    }

}