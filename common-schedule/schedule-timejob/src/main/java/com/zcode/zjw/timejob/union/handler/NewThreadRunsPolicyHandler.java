package com.zcode.zjw.timejob.union.handler;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zhangjiwei
 * @description 另起一个线程处理，直到资源不足抛出异常
 * @date 2022/11/5 下午4:57
 */
class NewThreadRunsPolicyHandler implements RejectedExecutionHandler {
    NewThreadRunsPolicyHandler() {
        super();
    }

    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            final Thread t = new Thread(r, "Temporary task executor");
            t.start();
        } catch (Throwable e) {
            throw new RejectedExecutionException(
                    "启动新线程失败！", e);
        }
    }
}