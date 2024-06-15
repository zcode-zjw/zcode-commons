package com.zcode.zjw.common.utils.file;

import sun.misc.MessageUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

/**
 * properties文件工具类
 *
 * @author zhangjiwei
 * @since 2023/8/5
 */
public class PropertiesUtil {
    
    public static <T> T getSpringItem(String fileName, String key) throws IOException {
        InputStream in = MessageUtils.class.getClassLoader().getResourceAsStream(fileName);
        Properties properties = new Properties();
        properties.load(in);
        String active = properties.getProperty("spring.profiles.active");
        if (null != active) {
            return getSpringItem(fileName + "-" + active, key);
        }

        return (T) properties.getProperty(key);
    }

    public static <T> T getItem(String key, File file) throws IOException {
        InputStream in = Files.newInputStream(file.toPath());
        Properties properties = new Properties();
        properties.load(in);

        return (T) properties.getProperty(key);
    }
    
}
