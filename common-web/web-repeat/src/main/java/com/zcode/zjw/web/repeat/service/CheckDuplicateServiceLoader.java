package com.zcode.zjw.web.repeat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangjiwei
 * @description 检测去重服务加载者
 * @date 2023/2/8 下午5:55
 */
@Component
public class CheckDuplicateServiceLoader {

    @Autowired
    private ApplicationContext applicationContext;

    public List<CheckDuplicateService> getCheckDuplicateServiceList() {
        List<CheckDuplicateService> checkDuplicateServiceList = new ArrayList<>();
        applicationContext.getBeansOfType(CheckDuplicateService.class).forEach((key, value) -> checkDuplicateServiceList.add(value));
        return checkDuplicateServiceList;
    }

}
