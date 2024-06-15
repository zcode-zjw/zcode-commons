package com.zcode.zjw.schedule.quartz.support;

public enum JobStatus {
    ENABLE("1"),
    DISABLE("0");
 
    private String value;
 
    private JobStatus(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return this.value;
    }
}