package com.zcode.zjw.timejob.union.infrastructure.exception;

/**
 * @author zhangjiwei
 * @description 缺少任务参数异常
 * @date 2022/11/5 上午9:44
 */
public class JobParamNotFoundException extends Exception {

    private static final long serialVersionUID = -3378498379613538774L;

    public JobParamNotFoundException(String param) {
        super("缺少参数：" + param);
    }

    public JobParamNotFoundException(String[] params) {
        super("缺少参数：" + getParams(params));
    }

    public static String getParams(String[] params) {
        StringBuilder string = new StringBuilder();
        for (String param : params) {
            string.append(param);
            string.append(", ");
        }
        return string.toString();
    }
}
