package com.zcode.zjw.schedule.quartz.listener;

import com.zcode.zjw.schedule.quartz.support.JobMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 定时任务监听器，容器启动时，添加定时任务到scheduler
 * 
 * @author zhangjiwei
 * @since 2022年05月09日
 */
@WebListener
@Component
public class BusinessJobListener implements ServletContextListener {
  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private JobMonitor jobMonitor;

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    log.info("init business job to scheduler");
    if (jobMonitor != null && !jobMonitor.isActive()) {
      jobMonitor.start();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    log.info("destroy business job of scheduler");
    if (jobMonitor != null && jobMonitor.isActive()) {
      jobMonitor.stop();
    }
  }
}
