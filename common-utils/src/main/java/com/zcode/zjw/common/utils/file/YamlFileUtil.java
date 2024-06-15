package com.zcode.zjw.common.utils.file;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * yaml/yml文件工具类
 *
 * @author zhangjiwei
 * @since 2023/8/5
 */
public class YamlFileUtil {

    public static <T> T getSpringItem(String fileName, String key) throws IOException {
        Yaml yaml = new Yaml();
        URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
        if (url != null) {
            //也可以将值转换为Map
            Map<String, Object> map = yaml.load(Files.newInputStream(Paths.get(url.getFile())));
            String activeKey = "spring.profiles.active";
            String activeVal = null;
            String[] split = activeKey.split("\\.");
            for (int i = 0; i < split.length; i++) {
                Object val = map.get(split[i]);
                if (null != val && i == split.length - 1) {
                    activeVal = (String) val;
                }
            }
            if (activeVal != null) {
                return getSpringItem(fileName + "-" + activeVal, key);
            }

            String[] keyArray = key.split("\\.");
            if (keyArray.length == 1) {
                return (T) map.get(key);
            }
            Map<String, Object> tmpMap = (Map<String, Object>) map.get(keyArray[0]);
            if (tmpMap == null) {
                return null;
            }
            for (int i = 1; i < keyArray.length; i++) {
                if (i == keyArray.length - 1) {
                    return (T) tmpMap.get(keyArray[i]);
                } else {
                    tmpMap = (Map<String, Object>) tmpMap.get(keyArray[i]);
                    if (tmpMap == null) {
                        return null;
                    }
                }
            }
        } else {
            throw new RuntimeException("找不到Spring配置文件（" + fileName + "）");
        }

        return null;
    }

    // yml2Map: Yaml to Map, 将yaml文件读为map
    public static Map<String, Object> ymlToMap(String path) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(path);
        Yaml yaml = new Yaml();
        return (Map<String, Object>) yaml.load(fileInputStream);
    }

    // map2Yml: Map to Yaml, 将map转换为yaml格式
    public static void mapToYml(Map<String, Object> map, String path) throws IOException {
        File file = new File(path);
        FileWriter fileWriter = new FileWriter(file);
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        yaml.dump(map, fileWriter);
    }

}
