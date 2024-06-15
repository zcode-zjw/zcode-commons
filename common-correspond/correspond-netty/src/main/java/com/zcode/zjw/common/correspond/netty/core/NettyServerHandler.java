package com.zcode.zjw.common.correspond.netty.core;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

public class NettyServerHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        // 数据分割符
        String delimiterStr = "##@##";
        ByteBuf delimiter = Unpooled.copiedBuffer(delimiterStr.getBytes());
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 使用自定义处理拆包/沾包，并且每次查找的最大长度为1024字节
        pipeline.addLast(new DelimiterBasedFrameDecoder(1024, delimiter));

        // 将上一步解码后的数据转码为Message实例
        pipeline.addLast(new MessageDecodeHandler());

        // 对发送客户端的数据进行编码，并添加数据分隔符
        pipeline.addLast(new MessageEncodeHandler(delimiterStr));

        // 对数据进行最终处理
        pipeline.addLast(new ServerListenerHandler());
    }
}