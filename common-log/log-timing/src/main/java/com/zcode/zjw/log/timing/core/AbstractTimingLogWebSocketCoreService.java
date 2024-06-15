package com.zcode.zjw.log.timing.core;

import cn.hutool.core.io.FileUtil;
import com.zcode.zjw.log.timing.util.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.ReversedLinesFileReader;

import javax.websocket.Session;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.WatchService;
import java.util.*;

/**
 * @author zhangjiwei
 * @description websocket核心服务抽象类
 * @date 2022/11/19 下午7:20
 */
@Slf4j
public abstract class AbstractTimingLogWebSocketCoreService {

    public abstract AsyncWebsocketService initAsyncWebsocketService();

    public abstract String fetchLogPath(Map<String, Object> pathParams);

    public void startLogListener(Session session, Map<Session, WatchService> watchRecordMap,
                                 LinkedList<String> logCacheList, Map<String, Object> pathParams) {
        String logPath = fetchLogPath(pathParams);
        // 第一次发送全部信息
        firstLoadFullLogText(session, logPath, logCacheList);
        // 监控的目录
        String filePath = FileUtil.getParent(logPath, 1);
        // 要监控的文件
        String fileName = FileUtil.getName(logPath);
        // 启动异步监听
        initAsyncWebsocketService().startListenLogFileAndSendWebsocket(session, filePath, fileName, watchRecordMap);
    }

    /**
     * 第一次加载发送全部日志
     *
     * @param session 会话
     * @param logPath 日志文件路径
     */
    private void firstLoadFullLogText(Session session, String logPath, LinkedList<String> logCacheList) {
        // 最长行数
        int maxRowNum = 400;
        // 读取最后500行，避免出现内存溢出
        List<String> logContentList = readLastLine(logPath, Charset.defaultCharset(), 500);
        // 将之前缓存的内容也算在内
        logCacheList.addAll(logContentList);
        // 将获取到的日志加入到缓存中
        for (int i = 0; i < logContentList.size(); i++) {
            if (i > (logContentList.size() - maxRowNum)) {
                logCacheList.add(logContentList.get(i) + "\n");
            }
        }
        // 如果缓存大小大于最大行数，将头节点一一移除
        while (logCacheList.size() > maxRowNum) {
            logCacheList.removeFirst();
        }
        // 写入结果字符串
        StringBuilder res = new StringBuilder();
        logCacheList.forEach(res::append);
        WebSocketUtil.sendMessage(res.toString(), session);
    }

    /**
     * 读取日志最后N行
     *
     * @param path
     * @param s
     * @param numLastLineToRead
     * @return
     */
    public static List<String> readLastLine(String path, Charset s, int numLastLineToRead) {
        File file = new File(path);
        List<String> result = new ArrayList<>();
        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(file, s)) {
            String line = "";
            while ((line = reader.readLine()) != null && result.size() < numLastLineToRead) {
                result.add(line);
            }
            //倒叙遍历
            Collections.reverse(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}