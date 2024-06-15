package com.zcode.zjw.schedule.quartz.support;

import com.zcode.zjw.common.utils.bean.SpringContextUtils;
import com.zcode.zjw.common.utils.common.StringUtil;
import com.zcode.zjw.schedule.quartz.model.BusinessJobConfig;
import com.zcode.zjw.schedule.quartz.util.CollectionUtil;
import com.zcode.zjw.schedule.quartz.util.IPUtil;
import com.zcode.zjw.schedule.quartz.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangjiwei
 * @since 2022年05月02日
 */
@Slf4j
public class BusinessJob extends AbstractJob {

    public static final String BUSINESS_JOB_GROUP_NAME = "egsc_job_group_name_business_jobs";
    private String jobName;
    private String cronExpression;

    public BusinessJob() {
    }

    public BusinessJob(String jobName, String cronExpression) {
        this.jobName = jobName;
        this.cronExpression = cronExpression;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @Override
    public String getName() {
        return this.jobName;
    }

    @Override
    public String getGroupName() {
        return BUSINESS_JOB_GROUP_NAME;
    }

    @Override
    public String getCronExpression() {
        return this.cronExpression;
    }

    /**
     * 所有的定时任务job类都是BusinessJob，执行BusinessJob的execute方法，
     * execute方法通过jobDataMap中获得BusinessJobConfig，从而获得具体执行类和方法信息，最后通过反射执行对应方法
     *
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        ApplicationContext applicationContext = SpringContextUtils.getApplicationContext();
        String appName = applicationContext.getEnvironment().getProperty("spring.application.name");
        if (StringUtil.isNotBlank(appName)) {
            BusinessJobConfig businessJobConfig = (BusinessJobConfig) jobDataMap.get("businessJobInfo");
            RedisUtils redisUtils = SpringContextUtils.getBean(RedisUtils.class);
            String jobName = jobExecutionContext.getJobDetail().getKey().getName();
            String serviceName = businessJobConfig.getServiceName();
            String methodName = businessJobConfig.getMethodName();
            // 执行类方法时需要传入的参数
            Map<String, Object> params = businessJobConfig.getParams();
            // 从spring上下文中拿到配置的定时任务执行类的实例
            Object obj = applicationContext.getBean(serviceName);
            if (null != obj) {
                Class clazz = obj.getClass();
                try {
                    Method method;
                    if (CollectionUtil.isNotBlank(params)) {
                        method = clazz.getDeclaredMethod(methodName, Map.class);
                    } else {
                        method = clazz.getDeclaredMethod(methodName);
                    }
                    if (null == method) {
                        log.error("执行job[{}]失败，没有找到任务的执行类实例{}的方法{}", jobName, serviceName, methodName);
                        return;
                    }
                    log.info("execute start: {}", businessJobConfig);
                    // 判断锁是否被占用
                    if (redisUtils.isValidLocked(jobName)) {
                        log.info("JOB正在运行中，jobName = " + jobName);
                        return;
                    }
                    // 加锁
                    if (!redisUtils.lockAtomic(jobName, 30)) {
                        log.info("JOB加锁失败，jobName = " + jobName);
                        return;
                    }

                    Map<String, String> jobMap = new HashMap<>();
                    jobMap.put("jobName", jobName);
                    jobMap.put("ip", IPUtil.getIP());
                    jobMap.put("port", applicationContext.getEnvironment().getProperty("server.port"));
                    // 保存任务信息到redis中，保存30分钟
                    redisUtils.set(jobName, jobMap, 1800);
                    // 执行任务类
                    if (null == params) {
                        // 反射调用无参方法
                        method.invoke(obj);
                    } else {
                        if (params.isEmpty()) {
                            method.invoke(obj);
                        } else {
                            // 反射调用有参数的方法
                            method.invoke(obj, params);
                        }
                    }
                    // 任务类执行结束, 删除jobMap
                    redisUtils.del(jobName);
                    log.info("execute end:" + businessJobConfig);
                    //Map<String, String> map = (Map<String, String>) redisUtils.get(jobName);
                    ////job没有在运行
                    //if (!isRunning(applicationContext, appName, map)) {
                    //    // 如果需要登录
                    //    //if (businessJobConfig.isRunAsAdmin()) {
                    //    //  if (!JWTUtil.checkToken(SecurityContext.getToken()).isStatus()) {
                    //    //    // 设置token
                    //    //
                    //    //  }
                    //    //}
                    //    Map<String, String> jobMap = new HashMap<>();
                    //    jobMap.put("jobName", jobName);
                    //    jobMap.put("ip", IPUtil.getIP());
                    //    jobMap.put("port", applicationContext.getEnvironment().getProperty("server.port"));
                    //    // 保存任务信息到redis中，保存30分钟
                    //    redisUtils.set(jobName, jobMap, 1800);
                    //    // 执行任务类
                    //    if (null == params) {
                    //        // 反射调用无参方法
                    //        method.invoke(obj);
                    //    } else {
                    //        // 反射调用有参数的方法
                    //        method.invoke(obj, params);
                    //    }
                    //    // 任务类执行结束, 删除jobMap
                    //    redisUtils.del(jobName);
                    //    log.info("execute end:" + businessJobConfig);
                    //}
                } catch (NoSuchMethodException e) {
                    log.error(e.getMessage(), e);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                } finally {
                    // 释放锁
                    redisUtils.unlock(jobName);
                }
            } else {
                log.error("执行job[{}]失败，没有找到任务的执行类实例{}", jobName, serviceName);
            }
        } else {
            log.error("error 缺少配置 spring.application.name");
        }
    }

    ///**
    // * 判断job是否已经在节点上执行
    // *
    // * @param applicationContext
    // * @param appName 应用名
    // * @param jobMap
    // * @return
    // */
    //private boolean isRunning(ApplicationContext applicationContext, String appName,
    //    Map<String, String> jobMap) {
    //  DiscoveryClient discoveryClient = applicationContext.getBean(DiscoveryClient.class);
    //  if (null != discoveryClient && CollectionUtil.isNotBlank(jobMap)
    //      && StringUtil.isNotBlank(appName)) {
    //    String ip = jobMap.get("ip");
    //    if (StringUtil.isNotBlank(ip)) {
    //      // 获取应用appName的其他注册实例
    //      List<ServiceInstance> instanceList = discoveryClient.getInstances(appName);
    //      if (CollectionUtil.isBlank(instanceList)) {
    //        return false;
    //      }
    //      if (instanceList.stream()
    //          .anyMatch(serviceInstance -> ip.equals(serviceInstance.getHost()))) {
    //        log.warn("同一JOB正在运行中，jobName = {}, 执行节点ip = {},本次job不执行", (String) jobMap.get("jobName"),
    //            ip);
    //        return true;
    //      }
    //    }
    //  }
    //  return false;
    //}

}
