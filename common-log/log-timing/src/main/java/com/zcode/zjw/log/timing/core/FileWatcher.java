package com.zcode.zjw.log.timing.core;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * @author zhangjiwei
 * @description 文件内容监控工具类
 * @date 2022/11/14 下午8:12
 */
@Slf4j
public class FileWatcher {
    /**
     * 文件监控
     * 同步调用会阻塞
     *
     * @param filePath
     * @param fileName
     * @param consumer
     */
    public static void watcherLog(WatchService watchService, String filePath, String fileName,
                                  Consumer<String> consumer, FileListenerStopCallback callback) {

        try {
            File configFile = Paths.get(filePath + File.separator + fileName).toFile();
            Paths.get(filePath).register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
            // 文件读取行数
            AtomicLong lastPointer = new AtomicLong(new RandomAccessFile(configFile, "r").length());
            do {
                if (callback.boolStop()) {
                    // 停止监听
                    break;
                }
                WatchKey key = null;
                try {
                    key = watchService.take();
                } catch (Exception e) {
                    break;
                }
                if (Objects.isNull(key)) {
                    log.error("获取 WatchKey 失败");
                    return;
                }
                List<WatchEvent<?>> watchEvents = key.pollEvents();
                watchEvents.stream().filter(
                        i -> StandardWatchEventKinds.ENTRY_MODIFY == i.kind()
                                && fileName.equals(((Path) i.context()).getFileName().toString())
                ).forEach(i -> {
                    if (i.count() > 1) {
                        return;
                    }
                    StringBuilder str = new StringBuilder();
                    // 读取文件
                    lastPointer.set(getFileContent(configFile, lastPointer.get(), str));

                    if (str.length() != 0) {
                        consumer.accept(str.toString());
                    }
                });
                key.reset();
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * beginPointer > configFile 时会从头读取
     *
     * @param configFile
     * @param beginPointer
     * @param str          内容会拼接进去
     * @return 读到了多少字节, -1 读取失败
     */
    private static long getFileContent(File configFile, long beginPointer, StringBuilder str) {
        if (beginPointer < 0) {
            beginPointer = 0;
        }
        RandomAccessFile file = null;
        boolean top = true;
        try {
            file = new RandomAccessFile(configFile, "r");
            if (beginPointer > file.length()) {
                return 0;
            }
            file.seek(beginPointer);
            String line;
            while ((line = file.readLine()) != null) {
                if (top) {
                    top = false;
                } else {
                    str.append("\n");
                }
                str.append(new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            }
            return file.getFilePointer();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}