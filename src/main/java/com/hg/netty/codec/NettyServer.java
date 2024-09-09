package com.hg.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

public class NettyServer {

    public static void main(String[] args) {

        // 创建两个线程组，BossGroup（处理连接请求）和WorkerGroup（真正处理业务请求）
        // 两个都是无线循环
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            // 创建服务器端启动参数，配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    // 使用NioServerSocketChannel作为服务器通道实现
                    .channel(NioServerSocketChannel.class)
                    // 设置线程队列等待连接的个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 创建一个通道初始化对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        // 给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 可以使用一个集合管理socketChannel
                            // 推送消息时，可以将业务加入到指定channel对应的nioEventLoop的taskQueue或者scheduleTaskQueue
                            System.out.println("客户socketChannel hashcode = " + socketChannel.hashCode());
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 加入一个ProtoBuf的解码器，指定解码对象
                            pipeline.addLast("decoder", new ProtobufDecoder(StudentPOJO.Student.getDefaultInstance()));
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            System.out.println("服务器准备好了...");
            // 绑定一个端口并同步，生成一个channelFuture对象
            ChannelFuture channelFuture = serverBootstrap.bind(16668).sync();

            // 给cf绑定监听器
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("监听端口16668成功");
                    } else {
                        System.out.println("监听端口16668失败");
                    }
                }
            });
            // 对channelFuture对象进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
