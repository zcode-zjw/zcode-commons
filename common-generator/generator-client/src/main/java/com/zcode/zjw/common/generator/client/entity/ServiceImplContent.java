package com.zcode.zjw.common.generator.client.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * ServiceImpl的实体类
 *
 * @author zhangjiwei
 */
public class ServiceImplContent {
    /**
     * ServiceImpl类的包名
     */
    private String classPackage;
    /**
     * ServiceImpl类的类型
     */
    private String className;
    /**
     * ServiceImpl类的属性
     */
    private Map<String, Object> item = new HashMap<>();

    /**
     * 初始化
     */
    public ServiceImplContent() {
        super();
    }

    /**
     * 通过包名与类名初始化
     *
     * @param classPackage
     * @param className
     */
    public ServiceImplContent(String classPackage, String className) {
        super();
        this.classPackage = classPackage;
        this.className = className;
    }

    public String getClassPackage() {
        return classPackage;
    }

    public void setClassPackage(String classPackage) {
        this.classPackage = classPackage;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, Object> getItem() {
        return item;
    }

    public void setItem(Map<String, Object> item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "ServiceImplContent [classPackage=" + classPackage + ", className=" + className + ", item=" + item + "]";
    }

}
