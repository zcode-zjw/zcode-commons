package com.zcode.zjw.timejob.union.manager.impl;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.function.Consumer;

/**
 * @author zhangjiwei
 * @description Job事务管理者
 * @date 2022/11/8 下午12:49
 */
@Component
public class JobTransactionManager extends AbstractDoTransaction {

    @Resource
    private AbstractDoTransaction DoTransaction;

    public void doAfterTransaction(Consumer<?> doFunc) {
        DoTransaction.setAfterCompletionFunc(doFunc);
    }

    public void doBeforeTransaction(Consumer<?> doFunc) {
        DoTransaction.setBeforeCompletionFunc(doFunc);
    }

}
