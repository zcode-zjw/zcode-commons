package com.zcode.zjw.log.trace.domain.core.subject.assist;

import com.zcode.zjw.log.trace.test.TestClass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomClassLoader extends ClassLoader {

    private static final String CLASS_FILE_SUFFIX = ".class";

    //AppClassLoader的父类加载器
    private ClassLoader extClassLoader;

    public Class loadClassFromPath(String className, String path) throws IOException {
        byte[] classBytes = Files.readAllBytes(Paths.get(path));
        return defineClass(className, classBytes, 0, classBytes.length);
    }

    public CustomClassLoader() {
        ClassLoader j = String.class.getClassLoader();
        if (j == null) {
            j = getSystemClassLoader();
            while (j.getParent() != null) {
                j = j.getParent();
            }
        }
        this.extClassLoader = j;
    }

    protected Class<?> loadClass(String name, boolean resolve) {

        Class cls;
        cls = findLoadedClass(name);
        if (cls != null) {
            return cls;
        }
        //获取ExtClassLoader
        ClassLoader extClassLoader = getExtClassLoader();
        try {
            //确保自定义的类不会覆盖Java的核心类
            //因为对于classpath上的类，ExtClassLoader是无法加载的，
            //但是如果这里换成AppClassLoader，它能加载classpath上的类，
            //显然会优先加载，也就轮不到自定义加载器了，就变成不同的双亲委派加载机制
            cls = extClassLoader.loadClass(name);
            if (cls != null) {
                return cls;
            }
        } catch (ClassNotFoundException e) {

        }
        cls = findClass(name);
        return cls;
    }

    @Override
    public Class<?> findClass(String name) {
        byte[] bt = loadClassData(name);
        return defineClass(name, bt, 0, bt.length);
    }

    private byte[] loadClassData(String className) {
        // 读取Class文件呢
        InputStream is = getClass().getClassLoader().getResourceAsStream(className.replace(".", "/") + CLASS_FILE_SUFFIX);
        ByteArrayOutputStream byteSt = new ByteArrayOutputStream();
        // 写入byteStream
        int len = 0;
        try {
            while ((len = is.read()) != -1) {
                byteSt.write(len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 转换为数组
        return byteSt.toByteArray();
    }

    public static void main(String[] args) throws Exception {
        CustomClassLoader classLoader = new CustomClassLoader();

        // 加载新的类
        //String newClassCode = "public class NewClass { public void printMessage() { System.out.println(\"Hello, NewClass!\"); } }";
        Path newClassFilePath = Paths.get("/Volumes/DataDisk/Item/zcode-commons/common-log/log-trace/target/classes/com/zcode/zjw/log/trace/test/TestClass.class");
        //Files.write(newClassFilePath, newClassCode.getBytes());

        Class<?> newClass = classLoader.loadClassFromPath("com.zcode.zjw.log.trace.test.TestClass", newClassFilePath.toString());
        //Object newInstance = newClass.getDeclaredConstructor().newInstance();
        //newClass.getMethod("printMessage").invoke(newInstance);
        //Class<?> aClass = customClassLoader.loadClass("com.zcode.zjw.log.trace.test.TestClass");
        TestClass testClass = (TestClass) newClass.getDeclaredConstructor().newInstance();
        testClass.testMethod(1);
    }

    public ClassLoader getExtClassLoader() {
        return extClassLoader;
    }
}
