package com.zcode.zjw.timejob.union.handler;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangjiwei
 * @description 间隔时间重试（一次）
 * @date 2022/11/5 下午4:56
 */
public class WaitSepTimeRejectedExecutionHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(final Runnable r, final ThreadPoolExecutor executor) {
        try {
            executor.getQueue().offer(r, 60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RejectedExecutionException("Interrupted waiting for BrokerService.worker");
        }
        throw new RejectedExecutionException("等待队列任务执行超时(60s)！");
    }
}
