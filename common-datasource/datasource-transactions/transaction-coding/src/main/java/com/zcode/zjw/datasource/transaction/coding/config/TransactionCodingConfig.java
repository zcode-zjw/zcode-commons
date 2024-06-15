package com.zcode.zjw.datasource.transaction.coding.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author zhangjiwei
 * @description 编程形式事务配置
 * @date 2023/2/13 上午11:19
 */
@Configuration
public class TransactionCodingConfig {

    @Bean("transactionCodingManager")
    public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
        DataSourceTransactionManager platformTransactionManager = new DataSourceTransactionManager();
        platformTransactionManager.setDataSource(dataSource);
        return platformTransactionManager;
    }

}
