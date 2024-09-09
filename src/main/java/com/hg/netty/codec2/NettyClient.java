package com.hg.netty.codec2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class NettyClient {

    public static void main(String[] args) {
        // 客户端需要一个事件循环组
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            // 客户端使用Bootstrap
            Bootstrap bootstrap = new Bootstrap();
            // 设置相关参数
            bootstrap.group(group)
                    // 设置客户端通道实现类（反射）
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 加入protoBuffer的编码器
                            pipeline.addLast("encoder", new ProtobufEncoder());
                            // 加入自己的处理器
                            pipeline.addLast(new NettyClientHandler());

                        }
                    });
            System.out.println("客户端初始化成功...");
            // 启动客户端连接服务端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 16668).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
        } finally {
            group.shutdownGracefully();
        }
    }
}
