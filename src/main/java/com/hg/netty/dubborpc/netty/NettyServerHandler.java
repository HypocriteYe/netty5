package com.hg.netty.dubborpc.netty;

import com.hg.netty.dubborpc.provider.impl.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private HelloServiceImpl helloService = new HelloServiceImpl();


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取客户端的消息，并调用服务
        System.out.println("msg = " + msg);
         // 要求每次发送的消息都是以HelloService#hello#开头
        if (msg.toString().startsWith("HelloService#hello#")) {
            String result = helloService.hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
