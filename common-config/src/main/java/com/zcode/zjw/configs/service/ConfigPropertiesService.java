package com.zcode.zjw.configs.service;


import com.zcode.zjw.configs.common.ConfigPropertiesTypeEnum;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author zhangjiwei
 * @description 配置文件服务接口
 * @date 2022/12/2 下午10:14
 */
public interface ConfigPropertiesService {

    /**
     * 获取配置文件数据
     *
     * @param configPropertiesType 配置文件类型
     * @param content              查询时所需要的一些条件内容
     * @return 主机应用数据列表
     */
    List<Map<String, Object>> findData(ConfigPropertiesTypeEnum configPropertiesType, Map<String, Object> content) throws IOException;

    /**
     * 添加配置文件数据
     *
     * @param configPropertiesType 配置文件类型
     * @param content              添加内容（键值对形式）
     * @return 成功条数
     * @throws IOException
     */
    int addData(ConfigPropertiesTypeEnum configPropertiesType, Map<String, String> content) throws IOException;

    /**
     * 修改配置文件数据
     *
     * @param configPropertiesType 配置文件类型
     * @param content              添加内容（键值对形式）
     * @return 成功条数
     * @throws IOException
     */
    int updateData(ConfigPropertiesTypeEnum configPropertiesType, Map<String, Object> content);

    /**
     * 批量更新配置文件数据
     *
     * @param contentList 修改内容列表
     * @return
     */
    int batchUpdateData(List<Map<String, Object>> contentList);

    /**
     * 删除配置文件数据
     *
     * @param configPropertiesType 配置文件类型
     * @param content              添加内容（键值对形式）
     * @return 成功条数
     * @throws IOException
     */
    int delData(ConfigPropertiesTypeEnum configPropertiesType, Map<String, Object> content);

}
