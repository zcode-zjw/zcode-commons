package com.zcode.zjw.timejob.union.handler.impl;

import com.zcode.zjw.timejob.common.JobParamsEntity;
import com.zcode.zjw.timejob.context.ITimeJobApplicationAdvice;
import com.zcode.zjw.timejob.union.handler.TaskExecuteChainHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

/**
 * @author zhangjiwei
 * @description 开始任务链
 * @date 2022/11/6 上午9:08
 */
@Component
public abstract class StartTaskExecuteChainHandler implements TaskExecuteChainHandler {

    @Resource
    protected ITimeJobApplicationAdvice timeJobApplicationAdvice;


    @Resource
    private ApplicationContext applicationContext;

    public abstract ScheduledFuture<?> execute(JobParamsEntity jobParamsEntity);

    /**
     * 获取开始任务的handler
     *
     * @return handler
     */
    public List<StartTaskExecuteChainHandler> getHandlers() {
        List<StartTaskExecuteChainHandler> handlerList = new ArrayList<>();
        Map<String, StartTaskExecuteChainHandler> handlersMap = applicationContext.getBeansOfType(StartTaskExecuteChainHandler.class);
        handlersMap.forEach((name, obj) -> handlerList.add(obj));
        // 获取头结点
        StartTaskExecuteChainHandler headerNode = handlerList.stream()
                .filter((handler) -> handler.beforeHandler() == null)
                .collect(Collectors.toList()).get(0);
        List<StartTaskExecuteChainHandler> handlers = new ArrayList<>();
        handlers.add(headerNode);
        searchNode(handlerList, handlers, headerNode);
        return groupSortHandler(handlers, handlerList);
    }

    /**
     * 查找Handler节点
     *
     * @param handlerList
     * @param dp
     * @param currentNode
     */
    private void searchNode(List<StartTaskExecuteChainHandler> handlerList, List<StartTaskExecuteChainHandler> dp,
                            StartTaskExecuteChainHandler currentNode) {
        for (StartTaskExecuteChainHandler handler : handlerList) {
            if (currentNode.nextHandler() == handler.getClass()) {
                dp.add(handler);
                searchNode(handlerList, dp, handler);
                break;
            }
        }
    }

    /**
     * 组内handler排序
     * @param handlers
     * @param handlerList
     * @return
     */
    private List<StartTaskExecuteChainHandler> groupSortHandler(List<StartTaskExecuteChainHandler> handlers, List<StartTaskExecuteChainHandler> handlerList) {
        List<StartTaskExecuteChainHandler> sortedHandler = new ArrayList<>();
        for (StartTaskExecuteChainHandler handler : handlers) {
            Map<StartTaskExecuteChainHandler, List<StartTaskExecuteChainHandler>> beforeHandlers = getBeforeHandlers(handlerList, handler);
            if (beforeHandlers.size() > 0 && beforeHandlers.get(handler).size() > 1) {
                List<StartTaskExecuteChainHandler> beforeHandlerList = beforeHandlers.get(handler);
                beforeHandlerList.sort(Comparator.comparing(StartTaskExecuteChainHandler::groupSortFlag));
                sortedHandler.addAll(beforeHandlerList);
            }
            else {
                if (beforeHandlers.size() == 0) {
                    sortedHandler.add(handler);
                } else {
                    sortedHandler.addAll(beforeHandlers.get(handler));
                }
            }
            sortedHandler.add(handler);
        }
        return sortedHandler.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 获取上一个节点列表
     *
     * @param handlerList
     * @param targetHandler
     * @return
     */
    private Map<StartTaskExecuteChainHandler, List<StartTaskExecuteChainHandler>> getBeforeHandlers(List<StartTaskExecuteChainHandler> handlerList, StartTaskExecuteChainHandler targetHandler) {
        Map<StartTaskExecuteChainHandler, List<StartTaskExecuteChainHandler>> resMap = new HashMap<>();
        for (StartTaskExecuteChainHandler handler : handlerList) {
            if (handler.nextHandler() == targetHandler.getClass()) {
                if (resMap.containsKey(targetHandler)) {
                    resMap.get(targetHandler).add(handler);
                } else {
                    List<StartTaskExecuteChainHandler> tmpList = new ArrayList<>();
                    tmpList.add(handler);
                    resMap.put(targetHandler, tmpList);
                }
            }
        }
        return resMap;
    }

}
