package com.zcode.zjw.datasource.transaction.coding.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author zhangjiwei
 * @description 编程式事务包装者
 * @date 2023/2/13 上午11:24
 */
@Component
@Slf4j
public class TransactionCodingWrapper {

    @Autowired
    private PlatformTransactionManager transactionCodingManager;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 操作包装
     * @param codeLogic 业务代码
     */
    public void wrapper(Consumer<?> codeLogic) {
        TransactionStatus transaction = transactionCodingManager.getTransaction(new DefaultTransactionDefinition());
        try {
            codeLogic.accept(null);
            transactionCodingManager.commit(transaction);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            transactionCodingManager.rollback(transaction);
        }
    }

    /**
     * 操作包装
     * @param codeLogic 业务代码
     */
    public <T> T wrapper(Supplier<T> codeLogic) {
        TransactionStatus transaction = transactionCodingManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Object res = codeLogic.get();
            transactionCodingManager.commit(transaction);
            return (T) res;
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            transactionCodingManager.rollback(transaction);
            return null;
        }
    }

    /**
     * 操作包装（对于单条sql）
     * @param codeLogic 业务代码
     */
    public void wrapperSingleSql(Consumer<?> codeLogic, String sql) {
        TransactionStatus transaction = transactionCodingManager.getTransaction(new DefaultTransactionDefinition());
        try {
            jdbcTemplate.execute(sql);
            codeLogic.accept(null);
            transactionCodingManager.commit(transaction);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            transactionCodingManager.rollback(transaction);
        }
    }
}
