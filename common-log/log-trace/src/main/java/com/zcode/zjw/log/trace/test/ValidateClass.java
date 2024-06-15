package com.zcode.zjw.log.trace.test;

/**
 * 描述
 *
 * @author zhangjiwei
 * @since 2023/8/3
 */
public class ValidateClass {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String classPath = "com.zcode.zjw.log.trace.test.TestClass";
        TestClass testClass = (TestClass) Class.forName(classPath).newInstance();
        testClass.testMethod(1);
    }

}
