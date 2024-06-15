package com.zcode.zjw.log.timing.core;

import com.zcode.zjw.log.timing.util.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.nio.file.WatchService;
import java.util.Map;

/**
 * @author zhangjiwei
 * @description 异步服务实现
 * @date 2022/11/14 下午8:11
 */
@Component
@Slf4j
public class AsyncWebsocketService {

    @Async("logFileListenerExecutor")
    public void startListenLogFileAndSendWebsocket(Session session, String filePath, String fileName,
                                                   Map<Session, WatchService> map) {
        try {
            log.info("开始监听 {} {}", filePath, fileName);
            FileWatcher.watcherLog(map.get(session), filePath, fileName, log -> WebSocketUtil.sendMessage(log, session), () -> {
                // 如果会话移除则停止监听 释放资源
                return !map.containsKey(session);
            });
            log.info("停止监听 {} {} 释放资源 返回主程序", filePath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
