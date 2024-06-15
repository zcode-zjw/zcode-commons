package com.zcode.zjw.configs.service.impl;

import com.zcode.zjw.configs.common.ConfigPropertiesTypeEnum;
import com.zcode.zjw.configs.common.PropertiesPathType;
import com.zcode.zjw.configs.service.ConfigPropertiesService;
import com.zcode.zjw.configs.utils.PropertiesUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangjiwei
 * @description 文件配置服务
 * @date 2022/12/2 下午10:15
 */
@Service
public class ConfigPropertiesServiceImpl implements ConfigPropertiesService {
    
    @Autowired
    private PropertiesUtil propertiesUtil;

    @SneakyThrows
    @Override
    public List<Map<String, Object>> findData(ConfigPropertiesTypeEnum configPropertiesType, Map<String, Object> content) {
        List<Map<String, Object>> res = new ArrayList<>();
        // 如果为null，说明为全模式，需要返回所有配置信息
        if (configPropertiesType == null) {
            for (ConfigPropertiesTypeEnum type : ConfigPropertiesTypeEnum.values()) {
                res.addAll(innerFindData(type, content));
            }
        } else {
            res.addAll(innerFindData(configPropertiesType, content));
        }
        return res;
    }

    /**
     * 查询配置信息主要逻辑
     *
     * @param configPropertiesType
     * @param content
     * @return
     * @throws IOException
     */
    private List<Map<String, Object>> innerFindData(ConfigPropertiesTypeEnum configPropertiesType, Map<String, Object> content) throws IOException {
        List<Map<String, Object>> res = new ArrayList<>();
        Properties properties;
        // 是否使用持久化
        if (Boolean.valueOf(content.get("persistentMode") + "").equals(true)) {
            properties = propertiesUtil.getProperties(configPropertiesType.getFileName(), PropertiesPathType.CONFIG);
        } else {
            properties = PropertiesLoaderUtils.loadAllProperties(configPropertiesType.getFileName() + ".properties");
        }
        Set<Object> keys = properties.keySet();
        int count = 0;
        for (Object key : keys) {
            count++;
            Map<String, Object> tmpMap = new HashMap<>();
            String[] dataRow = key.toString().split("\\.");
            if (dataRow.length == 1) {
                tmpMap.put("header", "");
                tmpMap.put("column", dataRow[0]);
            } else if (dataRow.length == 2) {
                tmpMap.put("header", dataRow[0]);
                tmpMap.put("column", dataRow[1]);
            } else {
                StringBuilder header = new StringBuilder();
                for (int i = 0; i < Arrays.asList(dataRow).size(); i++) {
                    if (i < dataRow.length - 1) {
                        header.append(dataRow[i]);
                        header.append(".");
                    }
                }
                tmpMap.put("header", header.toString().substring(0, header.length() - 1));
                tmpMap.put("column", dataRow[dataRow.length - 1]);
            }
            tmpMap.put("value", properties.get(key) + "");
            tmpMap.put("editable", false);
            tmpMap.put("key", count + "");
            // 排除过滤字段
            if (!fileFiled.contains(tmpMap.get("header"))) {
                res.add(tmpMap);
            }
        }
        return res;
    }

    @Override
    public int addData(ConfigPropertiesTypeEnum configPropertiesType, Map<String, String> content) {
        AtomicInteger res = new AtomicInteger();
//        Properties properties = PropertiesLoaderUtils.loadAllProperties(configPropertiesType.getFileName() + ".properties");
        Properties properties = propertiesUtil.getProperties(configPropertiesType.getFileName(), PropertiesPathType.CONFIG);
        content.forEach((key, value) -> {
            properties.put(key, value);
            res.getAndIncrement();
        });
        return res.get();
    }

    @SneakyThrows
    @Override
    public int updateData(ConfigPropertiesTypeEnum configPropertiesType, Map<String, Object> content) {
        AtomicInteger res = new AtomicInteger();
        Map<String, Object> data = (Map<String, Object>) content.get("data");
        if (data == null) {
            data = content;
        }
        String key = data.get("header") + "." + data.get("column");
        String value = data.get("value") + "";
        PropertiesPathType pathType;
        // 是否使用持久化
        if (content.get("persistentMode").equals(true)) {
            pathType = PropertiesPathType.CONFIG;
            propertiesUtil.setValue((String) content.get("fileName"), pathType, key, value);
        } else {
            // 修改内存对象的值
            PropertiesLoaderUtils.loadAllProperties(content.get("fileName") + ".properties").setProperty(key, value);
        }
        res.getAndIncrement();
        return res.get();
    }

    @Override
    public int batchUpdateData(List<Map<String, Object>> contentList) {
        int okNum = 0;
        for (Map<String, Object> content : contentList) {
            okNum = okNum + updateData(ConfigPropertiesTypeEnum.selectType((String) content.get("fileName")), content);
        }
        return okNum;
    }

    @SneakyThrows
    @Override
    public int delData(ConfigPropertiesTypeEnum configPropertiesType, Map<String, Object> content) {
        AtomicInteger res = new AtomicInteger();
        PropertiesPathType pathType;
        Map<String, Object> data = (Map<String, Object>) content.get("data");
        String key = data.get("header") + "." + data.get("column");
        String value = (String) data.get("value");
        String fileName = content.get("fileName") + "";
        // 是否使用持久化
        if (content.get("persistentMode").equals(true)) {
            pathType = PropertiesPathType.CONFIG;
            propertiesUtil.delKeyValue(fileName, pathType, key, value);
        } else {
            // 修改内存对象的值
            PropertiesLoaderUtils.loadAllProperties(content.get("fileName") + ".properties").setProperty(key, value);
        }
        res.getAndIncrement();
        return res.get();
    }

    /**
     * 排除字段
     */
    static List<String> fileFiled = new ArrayList<String>() {
        {
            add("arthas");
            add("mybatis-plus");
            add("spring.mvc.view");
            add("spring.mvc");
            add("spring.web.resources");
        }
    };


}
