package com.zcode.zjw.log.timing.api;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.fastjson.JSONObject;
import com.zcode.zjw.common.utils.bean.SpringContextUtils;
import com.zcode.zjw.log.timing.core.AbstractTimingLogWebSocketCoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实时日志接口
 *
 * @author zhangjiwei
 * @since 2023/8/6
 */
@Slf4j
@ServerEndpoint(value = "/timing/log/{pathJsonParams}")
@Component
@CrossOrigin
@Lazy
public class TimingLogWebsocketApi {

    /**
     * 传输日志缓存容器
     */
    private final LinkedList<String> logCacheList = new LinkedList<>();

    private AbstractTimingLogWebSocketCoreService timingLogWebSocketCoreService = SpringContextUtils.getBean(AbstractTimingLogWebSocketCoreService.class);

    /**
     * 绑定服务
     */
    private final static Map<Session, WatchService> watchRecordMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("pathJsonParams") String pathParams) throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        watchRecordMap.put(session, watchService);
        // 启动日志监听
        timingLogWebSocketCoreService.startLogListener(session, watchRecordMap, logCacheList, JSONObject.parseObject(pathParams));
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        watchRecordMap.get(session).close();
        watchRecordMap.remove(session);
        logCacheList.clear();
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        if (message.equals("wsStop")) {
            watchRecordMap.get(session).close();
            watchRecordMap.remove(session);
            logCacheList.clear();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误 {}", ExceptionUtil.getRootCauseMessage(error));
        error.printStackTrace();
        watchRecordMap.remove(session);
        logCacheList.clear();
    }

}
