package com.zcode.zjw.datasource.multiple.core;

import com.zcode.zjw.datasource.multiple.utils.DynamicDataSourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 创建多数据源类继承AbstractRoutingDataSource类，重写determineCurrentLookupKey()方法，
 * 用于获取当前线程中的指定的数据源key，通过该key拿到对应的数据源对象
 *
 * @author：zjw
 */
@Slf4j
public class MultiDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String key = DynamicDataSourceHolder.getDataSource();
        log.info("DataSource key ---> " + key);
        return key;
    }

}