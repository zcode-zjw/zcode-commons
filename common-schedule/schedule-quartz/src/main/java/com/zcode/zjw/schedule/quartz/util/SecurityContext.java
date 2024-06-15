package com.zcode.zjw.schedule.quartz.util;


/**
 * @author zhangjiwei
 * @since 2022年03月23日
 */
public class SecurityContext {
    // ThreadLocal封装一个变量，该变量只对当前线程可见，实现线程局部变量
    private static ThreadLocal userThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();

    private SecurityContext() {
    }

    //public static User getUserPrincipal() {
    //    return userThreadLocal.get();
    //}

    //public static void setUserPrincipal(User user) {
    //    userThreadLocal.set(user);
    //}

    public static String getToken() {
        return tokenThreadLocal.get();
    }

    public static void setToken(String token) {
        tokenThreadLocal.set(token);
    }

    //public static String getCourtUuid() {
    //    return getUserPrincipal().getCourtUuid();
    //}
}
