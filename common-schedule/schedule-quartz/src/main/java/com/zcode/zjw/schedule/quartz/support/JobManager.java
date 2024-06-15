package com.zcode.zjw.schedule.quartz.support;

import com.zcode.zjw.common.utils.common.StringUtil;
import com.zcode.zjw.schedule.quartz.model.BusinessJobConfig;
import com.zcode.zjw.schedule.quartz.util.CollectionUtil;
import com.zcode.zjw.schedule.quartz.util.IPUtil;
import com.zcode.zjw.schedule.quartz.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangjiwei
 * @since 2022年04月28日
 */
@Component
@Slf4j
public class JobManager implements ApplicationContextAware {

  private ApplicationContext applicationContext;
  private static String ip = "";
  @Value("${server.port}")
  private String port;
  @Autowired
  private RedisUtils redisUtils;

  private JobManager() {
    ip = IPUtil.getIP();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  public ApplicationContext getApplicationContext() {
    return this.applicationContext;
  }

  /**
   * 创建任务
   * 
   * @param jobConfig
   * @return
   */
  public boolean createJob(BusinessJobConfig jobConfig) {
    StringBuilder jobNameBuilder = new StringBuilder(AbstractJob.JOB_NAME_PREFIX)
        .append(jobConfig.getServiceName()).append("#").append(jobConfig.getMethodName());
    if (StringUtil.isNotBlank(jobConfig.getId())) {
      jobNameBuilder.append("#").append(jobConfig.getId());
    }
    String jobName = jobNameBuilder.toString();
    Scheduler scheduler = SchedulerManager.getInstance();
    try {
      JobKey jobKey = new JobKey(jobName, BusinessJob.BUSINESS_JOB_GROUP_NAME);
      if (scheduler.checkExists(jobKey)) {
        log.debug("--------- 创建任务失败。任务已存在," + jobName);
        return false;
      } else {
        log.info("系统的IP={}, 端口号port={}", ip, port);
        Map<String, String> jobMap = (Map<String, String>) redisUtils.get(jobName);
        if (null != jobMap && StringUtil.equals(ip, jobMap.get("ip"))
            && StringUtil.equals(port, jobMap.get("port"))) {
          redisUtils.del(jobName);
          log.info("--------- 删除过期的redis中的job[{}]", jobName);
        }
        BusinessJob businessJob = new BusinessJob(jobName, jobConfig.getCronExpression());
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("businessJobInfo", jobConfig);
        jobDataMap.put("applicationContext", applicationContext);
        businessJob.setJobDataMap(jobDataMap);
        this.addJob(businessJob);
      }
      return true;
    } catch (SchedulerException e) {
      log.error("Secheduler checkExists job[name={}, groupName={}] error", jobName,
          BusinessJob.BUSINESS_JOB_GROUP_NAME, e);
      return false;
    }
  }

  /**
   * 将任务和cron触发器添加到scheduler
   * 
   * @param job
   */
  public void addJob(AbstractJob job) {
    Scheduler scheduler = SchedulerManager.getInstance();
    String jobName = job.getName();
    String triggerName = "trigger_for_" + jobName;
    try {
      // 任务详细，包括任务名、组名、jobDataMap
      JobDetail jobDetail = JobBuilder.newJob(job.getClass())
          .withIdentity(jobName, job.getGroupName()).usingJobData(job.getJobDataMap()).build();
      // 任务触发器,设置触发器名、触发器组名、触发条件(cron触发器条件为cron表达式)
      Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, "trigger_group")
          .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression())).build();
      // 关联任务和触发器，并添加到调度器
      scheduler.scheduleJob(jobDetail, trigger);
      if (!scheduler.isShutdown() && !scheduler.isStarted()) {
        // 如果scheduler未关闭,且未开启，则开启scheduler
        scheduler.start();
      }
    } catch (SchedulerException e) {
      log.error("Add a time in [{}] triggered of the job [{}] is error!", job.getCronExpression(),
          job.getName(), e);
    }
    log.info("Add a job[{}] -- trigger cronExpression[{}]", jobName, job.getName());
  }

  /**
   * 修改任务的触发器
   * 
   * @param jobName
   * @param cronExpression
   */
  public void modifyJobTime(String jobName, String cronExpression) {
    Scheduler scheduler = SchedulerManager.getInstance();
    String triggerName = "trigger_for_" + jobName;
    TriggerKey triggerKey = new TriggerKey(triggerName, "trigger_group");
    try {
      // 通过TriggerKey获得要修改的触发器
      Trigger trigger = scheduler.getTrigger(triggerKey);
      if (null != trigger) {
        // 统一采用cron触发器
        CronTrigger cronTrigger = (CronTrigger) trigger;
        // 从新设置cron触发器的cron表达式
        cronTrigger.getTriggerBuilder()
            .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).startNow().build();
        // 重新启用触发器F
        scheduler.resumeTrigger(triggerKey);
      }
    } catch (SchedulerException e) {
      log.error(
          "Add a time in [" + cronExpression + "] triggered of the job [" + jobName + "] is error!",
          e);
    }
    log.debug("modify a job[{}] end.", jobName);
  }

  /**
   * 移除任务
   * 
   * @param jobName
   * @param jobGroupName
   * @return
   */
  public boolean removeJob(String jobName, String jobGroupName) {
    Scheduler scheduler = SchedulerManager.getInstance();
    JobKey jobKey = new JobKey(jobName, jobGroupName);
    try {
      log.debug("remove job[{}]", jobName);
      return scheduler.deleteJob(jobKey);
    } catch (SchedulerException e) {
      log.error("remove job[{}] error!", jobName, e);
      return false;
    }
  }

  /**
   * 任务是否存在
   * 
   * @param jobName
   * @param jobGroupName
   * @return
   */
  public boolean isJobExist(String jobName, String jobGroupName) {
    Scheduler scheduler = SchedulerManager.getInstance();
    JobKey jobKey = new JobKey(jobName, jobGroupName);
    try {
      return scheduler.checkExists(jobKey);
    } catch (SchedulerException e) {
      log.error("is job[{}] exist error", jobName, e);
      return false;
    }
  }

  /**
   * 获得所有正在运行的任务,包括jobDetail和trigger
   * 
   * @return
   */
  public List<Object[]> getAllRunningJob() {
    Scheduler scheduler = SchedulerManager.getInstance();
    try {
      List<JobExecutionContext> runningJobs = scheduler.getCurrentlyExecutingJobs();
      if (CollectionUtil.isBlank(runningJobs)) {
        return null;
      }
      return runningJobs.stream().map(jobExecutionContext -> new Object[] {
          jobExecutionContext.getJobDetail(), jobExecutionContext.getTrigger()})
          .collect(Collectors.toList());
    } catch (SchedulerException e) {
      log.error("find all jobs error!", e);
      return null;
    }
  }
}
