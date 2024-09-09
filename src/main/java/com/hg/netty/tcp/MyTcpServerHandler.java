package com.hg.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;


public class MyTcpServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String s = new String(bytes, CharsetUtil.UTF_8);
        System.out.println("服务端接收到消息: " + s);
        System.out.println("服务端接收到消息数量： " + ++count);
        // 服务端给客户端返回随机变量
        ByteBuf buf = Unpooled.copiedBuffer(UUID.randomUUID().toString(), CharsetUtil.UTF_8);
        channelHandlerContext.writeAndFlush(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
