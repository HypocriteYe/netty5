package com.hg.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyTcpClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyTcpClientHandler.channelActive");
        for (int i = 0; i < 10; i++) {
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello,server " + i, CharsetUtil.UTF_8);
            ctx.writeAndFlush(byteBuf);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf buf) throws Exception {
        System.out.println("MyTcpClientHandler.channelRead0");
        System.out.println("客户端接收到消息:" + buf.toString(CharsetUtil.UTF_8));
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String s = new String(bytes, CharsetUtil.UTF_8);
        System.out.println("客户端接收到消息数量： " + ++count);
    }
}
