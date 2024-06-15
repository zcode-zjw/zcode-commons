package com.zcode.zjw.schedule.quartz.support;

import com.zcode.zjw.schedule.quartz.model.BusinessJobConfig;
import com.zcode.zjw.schedule.quartz.model.JobProperties;
import com.zcode.zjw.schedule.quartz.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangjiwei
 * @since 2022年05月09日
 */
@Component
public class JobMonitor {
  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private JobProperties jobProperties;
  @Autowired
  private JobManager jobManager;
  // 表示任务配置是否激活
  private boolean active = false;

  public void start() {
    if (!this.active) {
      List<BusinessJobConfig> businessJobConfigs = jobProperties.getJobs();
      if (CollectionUtil.isNotBlank(businessJobConfigs)) {
        businessJobConfigs.forEach(jobConfig -> jobManager.createJob(jobConfig));
        this.active = true;
      } else {
        log.warn("未配置定时任务信息");
      }
    }
  }

  public void stop() {
    if (this.active) {
      this.active = true;
    }
  }

  public void restart() {
    this.stop();
    this.start();
  }

  public boolean isActive() {
    return this.active;
  }
}
