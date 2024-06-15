package com.zcode.zjw.timejob.union.infrastructure.exception;

/**
 * @author zhangjiwei
 * @description 任务未开始异常
 * @date 2022/11/5 上午9:44
 */
public class JobNotNullException extends Exception{

    private static final long serialVersionUID = -5259943306177958408L;

    public JobNotNullException(String msg) {
        super(msg);
    }

    public JobNotNullException() {
        super("任务不能为空！");
    }
}
