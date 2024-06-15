package com.zcode.zjw.log.user.common;

/**
 * @author zhangjiwei
 * @description 消息队列常量
 * @date 2022/11/27 下午2:05
 */
public enum MessageQueueEnum {

    ORDER_MESSAGE("operator_msg"),
    ID_MESSAGE("msg_id");

    MessageQueueEnum(String msgFlag) {
        this.msgFlag = msgFlag;
    }

    public String getMsgFlag() {
        return msgFlag;
    }

    private String msgFlag;

}
