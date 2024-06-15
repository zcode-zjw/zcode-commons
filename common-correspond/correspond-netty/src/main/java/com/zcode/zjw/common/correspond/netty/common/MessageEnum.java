package com.zcode.zjw.common.correspond.netty.common;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public enum MessageEnum {
    CONNECT(1, "心跳消息"),
    STATE(2, "设备状态");

    public final Integer type;
    public final String content;

    MessageEnum(Integer type, String content) {
        this.type = type;
        this.content = content;
    }

    // case中判断使用
    public static MessageEnum getStructureEnum(Message msg) {
        Integer type = Optional.ofNullable(msg)
                .map(Message::getMsgType)
                .orElse(0);
        if (type == 0) {
            return null;
        } else {
            List<MessageEnum> objectEnums = Arrays.stream(MessageEnum.values())
                    .filter((item) -> Objects.equals(item.getType(), type))
                    .distinct()
                    .collect(Collectors.toList());
            if (objectEnums.size() > 0) {
                return objectEnums.get(0);
            }
            return null;
        }
    }
    // setter、getter。。。。


    public Integer getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}