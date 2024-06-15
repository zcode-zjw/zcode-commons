package com.zcode.zjw.schedule.quartz.support;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author zhangjiwei
 * @since 2022年04月28日
 */
public class SchedulerManager {

    private static final Logger log = LoggerFactory.getLogger(SchedulerManager.class);
    private static Scheduler scheduler;

    private SchedulerManager() {
    }

    static {
        init();
    }

    public static Scheduler getInstance() {
        return scheduler;
    }

    /**
     * 初始化scehduler
     */
    public static void init() {
        Properties properties = new Properties();
        // 配置是否跳过更新检查
        properties.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");
        // 数据保存方式为持久化
        properties.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
        // ThreadPool 实现的类名
        properties.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        // 线程数量
        properties.setProperty("org.quartz.threadPool.threadCount", "5");
        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory(properties);
            scheduler = schedulerFactory.getScheduler();
        } catch (SchedulerException e) {
            log.error("The Scheduler initialization error!", e);
        }
        log.info("The Scheduler initialization completed!");
    }

    /**
     * 销毁scheduler
     */
    public static void destory() {
        try {
            if (scheduler != null && !scheduler.isShutdown()) {
                // true表示等待未完成的工作完成后才会关闭
                scheduler.shutdown(true);
                scheduler = null;
                log.info("The Scheduler shutdown completed!");
            }
        } catch (SchedulerException e) {
            log.error("The Scheduler shutdown error!", e);
        }
    }

}
