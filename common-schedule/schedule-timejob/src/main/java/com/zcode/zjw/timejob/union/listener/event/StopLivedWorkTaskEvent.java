package com.zcode.zjw.timejob.union.listener.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author zhangjiwei
 * @description 正在活跃的工作任务线程停止事件
 * @date 2022/11/7 下午7:50
 */
public class StopLivedWorkTaskEvent extends ApplicationEvent {

    private static final long serialVersionUID = 6092377391897732326L;

    public StopLivedWorkTaskEvent() {
        super(new Object());
    }

}
