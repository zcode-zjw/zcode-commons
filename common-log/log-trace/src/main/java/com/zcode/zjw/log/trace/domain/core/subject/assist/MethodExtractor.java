package com.zcode.zjw.log.trace.domain.core.subject.assist;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;

public class MethodExtractor {
    public static HashMap<String, String> extractMethods(String packageName) {
        HashMap<String, String> methodMap = new HashMap<>();

        // 将包名转换为文件路径
        String packagePath = packageName.replace(".", File.separator);

        // 获取类路径
        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);

        // 遍历类路径下的每个类文件
        for (String classpathEntry : classpathEntries) {
            File entryFile = new File(classpathEntry);
            if (entryFile.isDirectory()) {
                scanDirectory(entryFile, packagePath, methodMap);
            } else {
                scanClassFile(entryFile, packagePath, methodMap);
            }
        }

        return methodMap;
    }

    private static void scanDirectory(File directory, String packagePath, HashMap<String, String> methodMap) {
        // 递归扫描目录中的所有文件
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanDirectory(file, packagePath, methodMap);
                } else {
                    scanClassFile(file, packagePath, methodMap);
                }
            }
        }
    }

    private static void scanClassFile(File file, String packagePath, HashMap<String, String> methodMap) {
        String filePath = file.getAbsolutePath();
        if (filePath.endsWith(".class")) {
            // 获取类的全限定名
            String className = null;
            try {
                className = filePath.substring(filePath.indexOf(packagePath));
                className = className.substring(0, className.lastIndexOf('.')).replace(File.separator, ".");
                try {
                    // 加载类
                    Class<?> clazz = Class.forName(className);
                    // 获取类的所有方法
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods) {
                        // 获取方法的名称和源代码
                        String methodName = method.getName();
                        String methodSource = method.toString();

                        // 将方法名称和源代码添加到HashMap中
                        methodMap.put(methodName, methodSource);
                        System.out.println("methodName: " + methodName);
                        System.out.println("methodSource: " + methodSource);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                //
            }
        }
    }

    public static void main(String[] args) {
        String packageName = "com.zcode.zjw.log.trace"; // 指定要扫描的包名
        HashMap<String, String> methodMap = extractMethods(packageName);

        // 打印方法名称和源代码
        for (String methodName : methodMap.keySet()) {
            String methodSource = methodMap.get(methodName);
            System.out.println("Method Name: " + methodName);
            System.out.println("Method Source Code: " + methodSource);
            System.out.println();
        }
    }
}
