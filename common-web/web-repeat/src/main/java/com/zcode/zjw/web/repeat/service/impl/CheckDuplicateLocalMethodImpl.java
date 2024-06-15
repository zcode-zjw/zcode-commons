package com.zcode.zjw.web.repeat.service.impl;

import com.zcode.zjw.web.repeat.common.DeduplicateMode;
import com.zcode.zjw.web.repeat.service.CheckDuplicateService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangjiwei
 * @description 重复检测（通过本地方法表）
 * @date 2023/2/8 下午5:47
 */
@Service
public class CheckDuplicateLocalMethodImpl implements CheckDuplicateService {

    @Override
    public boolean repeatDataValidate(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        return false;
    }

    @Override
    public boolean supports(DeduplicateMode mode) {
        return mode.equals(DeduplicateMode.LOCAL_METHOD_TABLE);
    }

}
