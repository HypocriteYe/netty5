package com.hg.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 向管道中加入处理器
        // 加入一个netty提供的httpServerCodDec
        socketChannel.pipeline().addLast("myHttpServerCodec", new HttpServerCodec());
        socketChannel.pipeline().addLast("myHttpServerHandler", new HttpServerHandler());
        System.out.println("服务器初始化完成...");

    }
}
