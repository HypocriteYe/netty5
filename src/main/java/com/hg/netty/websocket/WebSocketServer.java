package com.hg.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {

    public static void main(String[] args) {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 在bossGroup增加一个日志处理器
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 因为基于http协议，所以要使用http的编解码器
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new ChunkedWriteHandler());
                            // http协议在传输过程中是分段的，httpObjectAggregator可以将多个段聚合
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            // 对应webSocket，数据是以帧的形式传递的，可以看到webSocketFrame下有6个子类
                            // ws://localhost:17000/和下面的/对应
                            // WebSocketServerProtocolHandler的功能是将http协议升级为ws协议，保持长连接
                            pipeline.addLast(new WebSocketServerProtocolHandler("/"));
                            // 自定义handler
                            pipeline.addLast(new MyTextWebSocketFrameHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(17000).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
