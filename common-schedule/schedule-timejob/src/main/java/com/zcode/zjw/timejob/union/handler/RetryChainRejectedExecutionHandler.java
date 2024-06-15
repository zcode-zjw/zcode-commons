package com.zcode.zjw.timejob.union.handler;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zhangjiwei
 * @description 执行链重试
 * @date 2022/11/5 下午4:57
 */
public class RetryChainRejectedExecutionHandler implements RejectedExecutionHandler {

    private final RejectedExecutionHandler[] handlerChain;

    public static RejectedExecutionHandler build(List<RejectedExecutionHandler> chain) {
        Objects.requireNonNull(chain, "执行链不能为空！");
        RejectedExecutionHandler[] handlerChain = chain.toArray(new RejectedExecutionHandler[0]);
        return new RetryChainRejectedExecutionHandler(handlerChain);
    }

    public RetryChainRejectedExecutionHandler(RejectedExecutionHandler[] handlerChain) {
        this.handlerChain = Objects.requireNonNull(handlerChain, "执行链不能为空！");
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        for (RejectedExecutionHandler rejectedExecutionHandler : handlerChain) {
            rejectedExecutionHandler.rejectedExecution(r, executor);
        }
    }
}
