package com.zcode.zjw.common.correspond.netty.core;

import com.zcode.zjw.common.correspond.netty.config.NettyProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Component
@Slf4j
public class NettyServerBoot {
    @Resource
    NioEventLoopGroup boosGroup;
    @Resource
    NioEventLoopGroup workerGroup;
    final ServerBootstrap serverBootstrap;
    final NettyProperties nettyProperties;

    public NettyServerBoot(ServerBootstrap serverBootstrap, NettyProperties nettyProperties) {
        this.serverBootstrap = serverBootstrap;
        this.nettyProperties = nettyProperties;
    }


    /**
     * 启动netty
     *
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {
        // 绑定端口启动
        serverBootstrap.bind(nettyProperties.getPort()).sync();
        // 备用端口
        serverBootstrap.bind(nettyProperties.getPortSalve()).sync();
        log.info("启动Netty: {}, {}", nettyProperties.getPort(), nettyProperties.getPortSalve());
    }

    /**
     * 关闭netty
     */
    @PreDestroy
    public void close() {
        log.info("正在关闭Netty...");
        boosGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}