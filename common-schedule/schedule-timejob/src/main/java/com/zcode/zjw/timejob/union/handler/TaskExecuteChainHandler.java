package com.zcode.zjw.timejob.union.handler;

/**
 * @author zhangjiwei
 * @description 任务链接口
 * @date 2022/11/6 上午9:04
 */
public interface TaskExecuteChainHandler {

    /**
     * 下一个handler
     *
     * @return
     */
    Class<? extends TaskExecuteChainHandler> nextHandler();

    /**
     * 上一个handler
     *
     * @return
     */
    Class<? extends TaskExecuteChainHandler> beforeHandler();

    /**
     * 设置组内排序标识
     *
     * @return
     */
    int groupSortFlag();

    boolean supports(TaskExecuteChainHandler handler);

}
