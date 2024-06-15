package com.zcode.zjw.schedule.quartz.service;

import com.zcode.zjw.schedule.quartz.model.BusinessJobConfig;
import com.zcode.zjw.schedule.quartz.support.JobManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class JobService {
    @Autowired
    private JobManager jobManager;

    private JobService() {
    }
 
    public boolean createJob(BusinessJobConfig jobConfig) {
        if (null == jobConfig) {
            log.info("参数jobConfig 不能为空！");
            return false;
        } else if (!StringUtils.isBlank(jobConfig.getServiceName()) && !StringUtils.isBlank(jobConfig.getMethodName()) && !StringUtils.isBlank(jobConfig.getCronExpression())) {
            if (null == jobConfig.getParams()) {
                jobConfig.setParams(new HashMap());
            }
            if (this.jobManager.createJob(jobConfig)) {
                log.info("创建任务成功 -> 服务名称：" + jobConfig.getServiceName() + " 方法名称：" + jobConfig.getMethodName() + " 表达式：" + jobConfig.getCronExpression() + " id：" + jobConfig.getId());
                return true;
            } else {
                log.info("创建任务失败 -> 任务已存在," + jobConfig.getServiceName() + " 方法名称：" + jobConfig.getMethodName() + " id：" + jobConfig.getId());
                return false;
            }
        } else {
            log.info("创建任务失败，由于服务名称、方法名称或表达式为空。服务名称：" + jobConfig.getServiceName() + " 方法名称：" + jobConfig.getMethodName() + " 表达式：" + jobConfig.getCronExpression() + " id：" + jobConfig.getId());
            return false;
        }
    }
 
    public boolean removeJob(String serviceName, String methodName) {
        return this.removeJob(serviceName, methodName, "");
    }
 
    public boolean removeJob(String serviceName, String methodName, String id) {
        if (!StringUtils.isBlank(serviceName) && !StringUtils.isBlank(methodName)) {
            return this.removeJob(this.getJobName(serviceName, methodName, id));
        } else {
            log.error("参数serviceName 和 methodName 不能为空！");
            return false;
        }
    }
 
    public boolean removeJob(String jobName) {
        if (StringUtils.isBlank(jobName)) {
            log.error("参数jobName 不能为空！");
            return false;
        } else {
            return this.jobManager.removeJob(jobName, "egsc_job_group_name_business_jobs");
        }
    }
 
    public boolean isJobExist(String serviceName, String methodName) {
        return this.isJobExist(serviceName, methodName, "");
    }
 
    public boolean isJobExist(String serviceName, String methodName, String id) {
        if (!StringUtils.isBlank(serviceName) && !StringUtils.isBlank(methodName)) {
            return this.isJobExist(this.getJobName(serviceName, methodName, id));
        } else {
            log.error("参数serviceName 和 methodName 不能为空！");
            return false;
        }
    }
 
    public boolean isJobExist(String jobName) {
        if (StringUtils.isBlank(jobName)) {
            log.error("参数jobName 不能为空！");
            return false;
        } else {
            return this.jobManager.isJobExist(jobName, "egsc_job_group_name_business_jobs");
        }
    }
 
    public List<Object[]> getAllRunningJob() {
        return this.jobManager.getAllRunningJob();
    }
 
    public String getJobName(String serviceName, String methodName, String id) {
        if (!StringUtils.isBlank(serviceName) && !StringUtils.isBlank(methodName)) {
            StringBuilder jobName = (new StringBuilder("egsc_job_name_")).append(serviceName).append("#").append(methodName);
            if (StringUtils.isNotBlank(id)) {
                jobName.append("#").append(id);
            }
 
            return jobName.toString();
        } else {
            return "";
        }
    }
}