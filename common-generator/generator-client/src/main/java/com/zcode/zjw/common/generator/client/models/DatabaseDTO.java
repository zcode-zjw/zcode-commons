package com.zcode.zjw.common.generator.client.models;

/**
 * 数据库连接DTO
 *
 * @author zhangjiwei
 */
public class DatabaseDTO {

    private String name;
    private int value;
    private String driverClass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    @Override
    public String toString() {
        return "DatabaseDTO [name=" + name + ", value=" + value + ", driverClass=" + driverClass + "]";
    }


}
