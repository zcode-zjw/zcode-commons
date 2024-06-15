package com.zcode.zjw.log.trace.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * 描述
 *
 * @author zhangjiwei
 * @since 2023/8/5
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TestService {

    @Autowired
    private TestClass testClass;

    public void test() {
        String s = testClass.testMethod(1);
    }

}
