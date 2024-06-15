package com.zcode.zjw.timejob.union.infrastructure.exception;

/**
 * @author zhangjiwei
 * @description 任务未开始异常
 * @date 2022/11/5 上午9:44
 */
public class JobNotStartException extends Exception{

    private static final long serialVersionUID = -5259943306177958408L;

    public JobNotStartException(String msg) {
        super(msg);
    }

    public JobNotStartException() {
        super("请先开始任务！");
    }
}
