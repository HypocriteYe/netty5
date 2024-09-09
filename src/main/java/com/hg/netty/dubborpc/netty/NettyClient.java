package com.hg.netty.dubborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.*;

public class NettyClient {

    private int count = 0;
    private static ExecutorService executor = new ThreadPoolExecutor(2, Runtime.getRuntime().availableProcessors(),
            10L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100), Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    private static NettyClientHandler handler;

    public static void initClient() {
        handler = new NettyClientHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(handler);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 17000).sync();
            System.out.println("客户端启动成功");
            // channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // group.shutdownGracefully();
        }
    }

    /**
     * 创建代理对象
     * @param serviceClass
     * @param providerName 协议头
     * @return
     */
    public Object getBean(final Class<?> serviceClass, String providerName) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[] {serviceClass}, (proxy, method, args) -> {
                    if (handler == null) {
                        initClient();
                    }
                    // 设置要发给服务端的信息
                    handler.setParam(providerName + args[0]);
                    System.out.println("客户端count: " + ++count);
                    return executor.submit(handler).get();
                });
    }
}
