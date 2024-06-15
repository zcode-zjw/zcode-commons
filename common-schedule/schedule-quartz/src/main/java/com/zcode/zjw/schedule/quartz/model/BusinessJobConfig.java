package com.zcode.zjw.schedule.quartz.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.Map;

/**
 * 定义任务属性
 *
 * @author zhangjiwei
 * @since 2022年04月28日
 */
@ToString
@AllArgsConstructor
public class BusinessJobConfig {
    private String serviceName;
    private String methodName;
    private String cronExpression;
    private String category;
    private String ips;
    private String status;
    private String creator;
    private String creatorId;
    private Date createDt;
    private String lastUpdator;
    private String lastUpdatorId;
    private Date lastUpdateDt;
    private boolean runAsAdmin = false;
    private Map<String, Object> params;
    private String id;

    public BusinessJobConfig() {
    }

    public boolean isRunAsAdmin() {
        return this.runAsAdmin;
    }

    public void setRunAsAdmin(boolean runAsAdmin) {
        this.runAsAdmin = runAsAdmin;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getCronExpression() {
        return this.cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    public Date getCreateDt() {
        return this.createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public String getLastUpdator() {
        return this.lastUpdator;
    }

    public void setLastUpdator(String lastUpdator) {
        this.lastUpdator = lastUpdator;
    }

    public String getLastUpdatorId() {
        return this.lastUpdatorId;
    }

    public void setLastUpdatorId(String lastUpdatorId) {
        this.lastUpdatorId = lastUpdatorId;
    }

    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    public Date getLastUpdateDt() {
        return this.lastUpdateDt;
    }

    public void setLastUpdateDt(Date lastUpdateDt) {
        this.lastUpdateDt = lastUpdateDt;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIps() {
        return this.ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public Map<String, Object> getParams() {
        return this.params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}