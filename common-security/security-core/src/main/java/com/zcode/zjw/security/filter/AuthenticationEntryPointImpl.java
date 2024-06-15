package com.zcode.zjw.security.filter;

import com.alibaba.fastjson.JSON;
import com.zcode.zjw.security.common.ExceptionCodeEnum;
import com.zcode.zjw.security.common.Result;
import com.zcode.zjw.security.utils.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangjiwei
 * @description
 * @date 2022/11/16 下午7:34
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        Result<?> result = Result.error(ExceptionCodeEnum.AUTH_ERROR, "认证失败请重新登录");
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response, json);
    }

}
