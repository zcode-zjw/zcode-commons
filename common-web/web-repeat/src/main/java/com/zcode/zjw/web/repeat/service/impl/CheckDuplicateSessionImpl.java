package com.zcode.zjw.web.repeat.service.impl;

import com.zcode.zjw.web.repeat.common.DeduplicateMode;
import com.zcode.zjw.web.repeat.service.CheckDuplicateService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangjiwei
 * @description 重复检测（通过Session）
 * @date 2023/2/8 下午5:47
 */
public class CheckDuplicateSessionImpl implements CheckDuplicateService {

    @Override
    public boolean repeatDataValidate(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        String nowUrlParams = getNowUrlParams(httpServletRequest);
        Object preUrlParams = httpServletRequest.getSession().getAttribute("repeatData");
        // 如果上一个数据为null,表示还没有访问页面
        if (preUrlParams == null) {
            httpServletRequest.getSession().setAttribute("repeatData", nowUrlParams);
            return false;
        }
        // 否则，已经访问过页面
        else {
            // 如果上次url+数据和本次url+数据相同，则表示重复添加数据
            if (preUrlParams.toString().equals(nowUrlParams)) {
                return true;
            }
            // 如果上次 url+数据 和本次url加数据不同，则不是重复提交
            else {
                httpServletRequest.getSession().setAttribute("repeatData", nowUrlParams);
                return false;
            }
        }
    }

    @Override
    public boolean supports(DeduplicateMode mode) {
        return mode.equals(DeduplicateMode.SESSION);
    }

}
