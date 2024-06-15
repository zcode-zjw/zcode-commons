package com.zcode.zjw.web.repeat.service;

import com.zcode.zjw.web.repeat.common.DeduplicateMode;
import com.zcode.zjw.web.repeat.utils.JsonMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangjiwei
 * @description 防重服务策略
 * @date 2023/2/8 下午5:44
 */
public interface CheckDuplicateService {

    /**
     * 获取全部请求内容
     *
     * @param httpServletRequest 请求
     * @return 请求内容
     */
    default String getNowUrlParams(HttpServletRequest httpServletRequest) {
        //String params=IdGen.generateShortUuid();
        //获取全部请求内容
        String params = JsonMapper.obj2String(httpServletRequest.getParameterMap());

        String url = httpServletRequest.getRequestURI();
        Map<String, String> map = new HashMap<>();
        map.put(url, params);
        return map.toString();
    }

    /**
     * 重复请求检测
     *
     * @param httpServletRequest 请求
     * @return 是否重复
     */
    boolean repeatDataValidate(HttpServletRequest httpServletRequest, HttpServletResponse response);


    boolean supports(DeduplicateMode mode);

}
