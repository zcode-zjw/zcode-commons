package com.zcode.zjw.log.trace.domain.timinglog.service;

import com.zcode.zjw.log.timing.core.AbstractTimingLogWebSocketCoreService;
import com.zcode.zjw.log.timing.core.AsyncWebsocketService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 实时日志WebSocket核心服务
 *
 * @author zhangjiwei
 * @since 2023/8/6
 */
@Service
public class TimingLogWebSocketCoreService extends AbstractTimingLogWebSocketCoreService {

    private final AsyncWebsocketService asyncWebsocketService;

    public TimingLogWebSocketCoreService(AsyncWebsocketService asyncWebsocketService) {
        this.asyncWebsocketService = asyncWebsocketService;
    }

    @Override
    public AsyncWebsocketService initAsyncWebsocketService() {
        return asyncWebsocketService;
    }

    @Override
    public String fetchLogPath(Map<String, Object> pathParams) {
        return null;
    }

}
