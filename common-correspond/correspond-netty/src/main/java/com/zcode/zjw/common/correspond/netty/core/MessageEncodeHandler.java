package com.zcode.zjw.common.correspond.netty.core;

import com.zcode.zjw.common.correspond.netty.common.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

public class MessageEncodeHandler extends MessageToByteEncoder<Message> {
    // 数据分割符
    String delimiter;

    public MessageEncodeHandler(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf out) throws Exception {
        out.writeBytes((message.toJsonString() + delimiter).getBytes(CharsetUtil.UTF_8));
    }
}