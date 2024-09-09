package com.hg.netty.handlerchain;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyServerHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Long aLong) throws Exception {
        System.out.println("从客户端" + channelHandlerContext.channel().remoteAddress().toString().substring(1) + "读取到long" + aLong);
        channelHandlerContext.writeAndFlush(98765L);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("MyServerHandler发生异常: " + cause.getMessage());
        ctx.close();
    }
}
