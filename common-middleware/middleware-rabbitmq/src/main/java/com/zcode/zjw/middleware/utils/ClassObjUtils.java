package com.zcode.zjw.middleware.utils;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author zhangjiwei
 * @description 类/对象工具
 * @date 2023/2/16 上午11:08
 */
public class ClassObjUtils {

    /**
     * 获取类的子类实例对象
     *
     * @param clazz 类的字节码对象
     * @return 类的子类实例对象
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static List getSubInstanceList(Class<?> clazz) throws IllegalAccessException, InstantiationException {
        List res = new ArrayList();
        // 基础扫描包
        String basePackages = "com.zcode.zjw";
        // 初始化工具类
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(basePackages).addScanners(new SubTypesScanner()).addScanners(new FieldAnnotationsScanner()));
        // 获取子类
        Set<Class<?>> subTypesOf = reflections.getSubTypesOf((Class<Object>) clazz);
        for (Class<?> aClass : subTypesOf) {
            res.add(aClass.newInstance());
        }
        return res;
    }

    /**
     * 获取类的子类字节码对象
     *
     * @param clazz 类的字节码对象
     * @return 类的子类字节码对象
     */
    public static Set<Class> getSubTypeList(Class<?> clazz) {
        // 基础扫描包
        String basePackages = "com.zcode.zjw";
        // 初始化工具类
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(basePackages).addScanners(new SubTypesScanner()).addScanners(new FieldAnnotationsScanner()));
        // 获取子类
        return reflections.getSubTypesOf((Class) clazz);
    }

}
