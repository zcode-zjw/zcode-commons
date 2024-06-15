package com.zcode.zjw.timejob.union.listener;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.zcode.zjw.timejob.context.ITimeJobApplicationAdvice;
import com.zcode.zjw.timejob.union.infrastructure.annotation.JobListener;
import com.zcode.zjw.timejob.union.listener.event.StopLivedWorkTaskEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

import javax.annotation.Resource;

/**
 * @author zhangjiwei
 * @description 关闭正在活跃的任务池线程
 * @date 2022/11/7 下午7:58
 */
@JobListener
@Slf4j
public class StopWorkTaskLivedAdvice implements ApplicationListener<StopLivedWorkTaskEvent>, BaseTimeJob {

    @Resource
    private ITimeJobApplicationAdvice timeJobApplicationAdvice;

    @Override
    public void onApplicationEvent(StopLivedWorkTaskEvent event) {
        closeLivedThread();
    }

    protected void closeLivedThread() {
        ConcurrentHashSet<Thread> workTaskLivedExecutor = timeJobApplicationAdvice.getWorkTaskLivedExecutor();
        if (workTaskLivedExecutor.size() == 0) {
            log.warn("当前无活跃的任务池线程！");
            return;
        }
        log.info("正在关闭活跃的任务池线程（当前存活数：" + workTaskLivedExecutor.size()+ "）");
        workTaskLivedExecutor.forEach((thread) -> {
            if (!thread.isInterrupted()) {
                thread.interrupt();
                log.warn("关闭线程 - " + thread.getName() + "，剩余活跃数：" + (workTaskLivedExecutor.size() - 1));
            }
        });
        log.info("活跃的任务池线程关闭！");
        workTaskLivedExecutor.clear();
    }
}
