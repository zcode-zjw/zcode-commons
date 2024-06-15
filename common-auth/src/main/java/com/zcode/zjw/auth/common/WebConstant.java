package com.zcode.zjw.auth.common;

public abstract class WebConstant {
    /**
     * 当前登录的用户信息（Session）
     */
    public static final String CURRENT_USER_IN_SESSION = "current_user_in_session";
    /**
     * 当前登录的用户信息（ThreadLocal）
     */
    public static final String USER_INFO = "user_info";
    /**
     * 当前用户拥有的权限
     */
    public static final String USER_PERMISSIONS = "user_permissions";
    /**
     * token的subject
     */
    public static final String USER_TOKEN_SUBJECT = "zcode_plb_subject_zjw";
    /**
     * 前端头信息携带token的key
     */
    public static final String TOKEN_AUTH_HEADER_KEY = "AUTH-TOKEN";
}