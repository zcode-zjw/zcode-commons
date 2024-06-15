package com.zcode.zjw.common.utils.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring配置文件工具类
 *
 * @author zhangjiwei
 * @since 2023/8/6
 */
@Slf4j
public class SpringConfigFileUtil {

    public static Map<String, String> getSpringConfigFileInfoMap() throws IOException {
        String pattern = "classpath*:application.{properties,yaml,yml}"; // 可以替换为其他扩展名或文件路径模式
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(pattern);
        Map<String, String> fileInfoMap = new HashMap<>();

        for (Resource resource : resources) {
            String[] split = resource.getFilename().split("\\.");
            String type = split[split.length - 1];
            String existType = fileInfoMap.get("type");
            if (fileInfoMap.isEmpty() || type == null) {
                fileInfoMap.put("name", resource.getFilename());
                fileInfoMap.put("type", type);
            }
            if ("properties".equals(type)) {
                fileInfoMap.put("name", resource.getFilename());
                fileInfoMap.put("type", type);
            } else if ("yaml".equals(type) && !"properties".equals(existType)) {
                fileInfoMap.put("name", resource.getFilename());
                fileInfoMap.put("type", type);
            } else if ("yml".equals(type) && !"properties".equals(existType) && !"yaml".equals(existType)) {
                fileInfoMap.put("name", resource.getFilename());
                fileInfoMap.put("type", type);
            }
        }
        return fileInfoMap;
    }

    public static <T> T getValueByKeyOnSpringConfigFile(String key, String defaultValue) {
        try {
            Map<String, String> springConfigFileInfoMap = getSpringConfigFileInfoMap();
            if (!springConfigFileInfoMap.isEmpty()) {
                String fileName = springConfigFileInfoMap.get("name");
                String fileType = springConfigFileInfoMap.get("type");
                if ("properties".equals(fileType)) {
                    T item = PropertiesUtil.getSpringItem(fileName, key);
                    return item == null ? (T) defaultValue : item;
                } else if ("yaml".equals(fileType) || "yml".equals(fileType)) {
                    T item = YamlFileUtil.getSpringItem(fileName, key);
                    return item == null ? (T) defaultValue : item;
                }
            }
        } catch (IOException e) {
            log.error("获取Spring配置文件内容失败", e);
        }

        return (T) defaultValue;
    }

}
