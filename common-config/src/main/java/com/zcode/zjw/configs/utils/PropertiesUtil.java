package com.zcode.zjw.configs.utils;

import com.zcode.zjw.configs.common.PropertiesPathType;
import com.zcode.zjw.configs.properties.ZcodeProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.Properties;

/**
 * @author zhangjiwei
 * @description Properties工具类
 * @since 2019/3/26 17:05
 */
@Slf4j
@Component
public class PropertiesUtil {

    @Resource
    private ZcodeProperties zcodeProperties;

    /**
     * 获取Properties文件绝对路径
     *
     * @param type 类型
     * @param fileName 文件名称
     * @return
     */
    private String getPropertiesPath(PropertiesPathType type, String fileName) {
        String res = "";
        if (PropertiesPathType.CLASSES.equals(type)) {
            res = Thread.currentThread().getContextClassLoader().getResource("").getPath() + fileName;
        } else if (PropertiesPathType.RESOURCE.equals(type)) {
            // application文件修改在classes下
            if ("application".equals(fileName)) {
                res = Thread.currentThread().getContextClassLoader().getResource("").getPath().replace("target/classes/", "") + "src/main/resources/" + fileName;
            } else {
                res = Thread.currentThread().getContextClassLoader().getResource("").getPath().replace("target/classes/", "") + "config/" + fileName;
            }
        } else if (PropertiesPathType.CONFIG.equals(type)) {
            res = zcodeProperties.getOwnSiteDir() + "/config/" + fileName;
        }
        return res;
    }

    /**
     * 获取Properties对象
     *
     * @return
     */
    public Properties getProperties(String fileName, PropertiesPathType pathType) {
//        if ("application".equals(fileName)) {
//            try {
//                return PropertiesLoaderUtils.loadAllProperties(fileName + ".properties");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(getPropertiesPath(pathType, fileName) + ".properties");
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            // 如果是application.properties文件，从resource下找
            if ("application".equals(fileName)) {
                try {
                    inputStream = new FileInputStream(getPropertiesPath(PropertiesPathType.RESOURCE, fileName) + ".properties");
                    properties.load(inputStream);
                } catch (IOException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                    log.warn("properties文件: {} 未找到!", fileName);
                }
            } else {
                log.warn("properties文件: {} 未找到!", fileName);
            }
        } catch (IOException e) {
            log.warn("出现IOException");
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.warn("properties文件: {} 流关闭出现异常!", fileName);
            }
        }
        return properties;
    }

    /**
     * 根据key查询value值
     *
     * @param key key
     * @return
     */
    public String getValue(String key, String fileName, PropertiesPathType pathType) {
        Properties properties = getProperties(fileName, pathType);
        return properties.getProperty(key);
    }

    /**
     * 新增/修改数据
     *
     * @param key
     * @param value
     */
    public void setValue(String fileName, PropertiesPathType propertiesPathType, String key, String value) {
        Properties properties = getProperties(fileName, propertiesPathType);
        properties.setProperty(key, value);
        String path = getPropertiesPath(propertiesPathType, fileName) + ".properties";
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            properties.store(fileOutputStream, "注释");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                System.out.println("properties文件流关闭出现异常");
            }
        }
    }


    /**
     * 添加配置项
     *
     * @param fileName
     * @param propertiesPathType
     * @param key
     * @param value
     */
    public void addKeyValue(String fileName, PropertiesPathType propertiesPathType, String key, String value) {
        Properties properties = getProperties(fileName, propertiesPathType);
        properties.put(key, value);
        String path = getPropertiesPath(propertiesPathType, fileName) + ".properties";

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            properties.store(fileOutputStream, "注释");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                System.out.println("properties文件流关闭出现异常");
            }
        }
    }

    /**
     * 删除配置项
     *
     * @param fileName
     * @param propertiesPathType
     * @param key
     * @param value
     */
    public void delKeyValue(String fileName, PropertiesPathType propertiesPathType, String key, String value) {
        Properties properties = getProperties(fileName, propertiesPathType);
        properties.remove(key, value);
        String path = getPropertiesPath(propertiesPathType, fileName) + ".properties";

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            properties.store(fileOutputStream, "注释");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                System.out.println("properties文件流关闭出现异常");
            }
        }
    }

}