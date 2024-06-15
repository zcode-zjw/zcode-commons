package com.zcode.zjw.datasource.multiple.utils;

import com.zcode.zjw.datasource.multiple.common.Const;

/**
 * 主要用于设置当前线程下数据源切换时的数据源唯一标识key，以便获取指定的数据源
 *
 * @author：zjw
 */
public class DynamicDataSourceHolder {

    private static final ThreadLocal<String> DATA_SOURCE_THEAD_LOCAL =
            ThreadLocal.withInitial(() -> Const.DEFAULT);


    public static String getDataSource() {
        return DATA_SOURCE_THEAD_LOCAL.get();
    }

    public static void setDataSource(String dataSource) {
        DATA_SOURCE_THEAD_LOCAL.set(dataSource);
    }

    public static void remove() {
        DATA_SOURCE_THEAD_LOCAL.remove();
    }

}