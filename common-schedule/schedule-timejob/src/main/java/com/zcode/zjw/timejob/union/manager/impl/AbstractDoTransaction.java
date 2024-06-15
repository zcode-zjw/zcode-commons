package com.zcode.zjw.timejob.union.manager.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;

import java.util.function.Consumer;

/**
 * @author zhangjiwei
 * @description
 * @date 2022/11/8 下午12:59
 */
@Component
public abstract class AbstractDoTransaction implements TransactionSynchronization {

    /**
     * 事务开始前执行的相关逻辑
     */
    private Consumer<?>  beforeCompletionFunc;

    /**
     * 事务开始后执行的相关逻辑
     */
    private Consumer<?>  afterCompletionFunc;

    /**
     * 事务提交前执行的相关逻辑
     */
    private Consumer<?>  beforeCommitFunc;

    /**
     * 事务提交后执行的相关逻辑
     */
    private Consumer<?>  afterCommitFunc;

    @Override
    public void suspend() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void flush() {

    }

    @Override
    public void beforeCommit(boolean readOnly) {
        if (readOnly) {
            beforeCommitFunc.accept(null);
        }
    }

    @Override
    public void beforeCompletion() {
        this.beforeCompletionFunc.accept(null);
    }

    @Override
    public void afterCommit() {
        afterCommitFunc.accept(null);
    }

    @Override
    public void afterCompletion(int status) {
        if (TransactionSynchronization.STATUS_COMMITTED == status) {
            afterCompletionFunc.accept(null);
        }
    }

    public Consumer<?> getBeforeCompletionFunc() {
        return beforeCompletionFunc;
    }

    protected void setBeforeCompletionFunc(Consumer<?> beforeCompletionFunc) {
        this.beforeCompletionFunc = beforeCompletionFunc;
    }

    public Consumer<?> getAfterCompletionFunc() {
        return afterCompletionFunc;
    }

    protected void setAfterCompletionFunc(Consumer<?> afterCompletionFunc) {
        this.afterCompletionFunc = afterCompletionFunc;
    }
}
