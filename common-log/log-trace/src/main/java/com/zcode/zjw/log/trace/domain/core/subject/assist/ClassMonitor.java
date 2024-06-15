package com.zcode.zjw.log.trace.domain.core.subject.assist;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Class监控
 *
 * @author zhangjiwei
 * @since 2023/8/5
 */
@Component
public class ClassMonitor {

    @Value("${trace.log.monitor.scan:com.zcode.zjw}")
    private String scanPackage;

    //@PostConstruct
    public void initMonitor() {
        ScheduledThreadPoolExecutor schedules = new ScheduledThreadPoolExecutor(1);
        ClassLoader classLoader;
        String filePath = (classLoader = ClassLoader.getSystemClassLoader())
                .getResource(scanPackage.replace(".", "/")).getPath()
                .substring(1);
        filePath = File.separator + filePath;
        try {
            Map<String, String> maps = Files.list(Paths.get(filePath))
                    .filter(x -> x.getFileName().toString().endsWith(".class"))
                    .collect(Collectors.toMap(m -> m.toString(), m -> {
                        try {
                            return String.valueOf(Files.size(m));
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }, (a, b) -> a));
            maps.forEach((m, n) -> System.out.println(m + " : " + n));

            String finalFilePath = filePath;
            schedules.scheduleAtFixedRate(() -> {
                try {
                    Path[] paths = Files.list(Paths.get(finalFilePath)).toArray(Path[]::new);
                    for (Path string : paths) {
                        String class_path = string.toString();
                        String last_time = String.valueOf(Files.size(string));
                        System.out.println("热监听中...");
                        if (Objects.equals(last_time, maps.get(class_path))) continue;
                        String pack_name = class_path.split("classes")[1].replace("\\", ".").substring(1);
                        String class_name = pack_name.substring(0, pack_name.lastIndexOf("."));
                        LoadClassInfo loadClassInfos = new LoadClassInfo(classLoader, scanPackage);
                        Class<?> classes = loadClassInfos.loadClass(class_name);
                        classes.newInstance();
                        maps.put(class_path, last_time);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 1, 1, TimeUnit.SECONDS);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
