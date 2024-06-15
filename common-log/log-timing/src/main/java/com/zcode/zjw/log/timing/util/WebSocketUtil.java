package com.zcode.zjw.log.timing.util;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;

/**
 * @author zhangjiwei
 * @description 发送消息的工具类
 * @date 2022/11/14 下午8:14
 */
@Slf4j
public class WebSocketUtil {
    /**
     * 服务端发送消息给客户端
     */
    public static void sendMessage(String message, Session toSession) {
        try {
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }
}
