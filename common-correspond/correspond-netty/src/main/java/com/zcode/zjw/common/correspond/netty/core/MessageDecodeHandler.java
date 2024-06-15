package com.zcode.zjw.common.correspond.netty.core;

import com.zcode.zjw.common.correspond.netty.common.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

public class MessageDecodeHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        ByteBuf frame = in.retainedDuplicate();
        final String content = frame.toString(CharsetUtil.UTF_8);
        Message message = new Message(content);
        list.add(message);
        in.skipBytes(in.readableBytes());
    }
}