package com.zcode.zjw.timejob.context;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.zcode.zjw.timejob.union.listener.event.impl.TimeJobPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * @author zhangjiwei
 * @description 定时任务环境抽象
 * @date 2022/11/6 下午8:09
 */
@Slf4j
public abstract class AbstractTimeJobApplication implements ITimeJobApplicationAdvice {

    @Resource
    protected ThreadPoolTaskScheduler threadPoolTimeTaskScheduler;

    @Resource
    protected ThreadPoolExecutor threadPoolExecutor;

    @Resource
    protected ConcurrentHashSet<Thread> workTaskThreadSet;

    @Resource
    protected ConcurrentHashMap<String, ConcurrentLinkedQueue<ScheduledFuture<?>>> jobFutureMap;

    @Resource
    protected TimeJobPublisher timeJobPublisher;

    protected ReentrantLock mutexLock = new ReentrantLock();

    protected void closeApplication(Consumer<?> stopMethodThread) throws InterruptedException {
        log.warn("正在关闭定时任务上下文环境...");
        // 先关闭任务线程池
        int count = 0;
        while (true) {
            Thread.sleep(1000);
            count++;
            log.warn("尝试任务线程池中..." + count + "s");
            if (!threadPoolExecutor.isShutdown()) {
                stopMethodThread.accept(null);
            }
            if (threadPoolExecutor.isTerminated()) {
                break;
            }
        }
        log.info("任务线程池已关闭！");
        threadPoolTimeTaskScheduler.shutdown();
        log.info("任务调度池已关闭！");
        log.info("定时任务环境已关闭！");
    }

}
