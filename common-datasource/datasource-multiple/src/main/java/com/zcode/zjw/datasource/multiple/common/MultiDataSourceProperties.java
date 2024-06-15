package com.zcode.zjw.datasource.multiple.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 多数据源属性类
 *
 * @author：zjw
 */
@Component
@ConfigurationProperties(prefix = Const.CONFIG_PREFIX)
public class MultiDataSourceProperties {

    private Map<String, DataSourceProp> dataSourcePropMap;

    public Map<String, DataSourceProp> getDataSourcePropMap() {
        return dataSourcePropMap;
    }

    public void setDataSourcePropMap(Map<String, DataSourceProp> dataSourcePropMap) {
        this.dataSourcePropMap = dataSourcePropMap;
    }
}