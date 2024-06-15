package com.zcode.zjw.auth.interception;

import com.zcode.zjw.auth.common.LoginExtendPointType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录扩展接口
 *
 * @author zhangjiwei
 * @date 2023/6/4
 */
public interface BaseLoginExtend {

    boolean flowPass(Object params, Object res, HttpServletRequest request, HttpServletResponse response);

    String failureMsg();

    LoginExtendPointType point();

}
