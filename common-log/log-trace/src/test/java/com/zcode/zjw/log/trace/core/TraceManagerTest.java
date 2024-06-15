package com.zcode.zjw.log.trace.core;

import com.zcode.zjw.log.trace.domain.core.service.TraceMethodManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 描述
 *
 * @author zhangjiwei
 * @since 2023/8/4
 */
@SpringBootTest
public class TraceManagerTest {

    @Autowired
    private TraceMethodManager testManager;

    @Test
    public void mainTest() throws Exception {
        String classPath = "com/zcode/zjw/log/trace/test/TestClass";
        //testManager.startTrace(classPath, "testMethod", "com/zcode/zjw/log/trace");
        //backup("TestClass", classPath);
    }

}