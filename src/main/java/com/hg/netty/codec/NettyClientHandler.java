package com.hg.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接成功");
        // ctx.writeAndFlush(Unpooled.copiedBuffer("hello,服务端~", CharsetUtil.UTF_8));
        StudentPOJO.Student student = StudentPOJO.Student.newBuilder().setId(4).setName("豹子头 林冲").build();
        ctx.writeAndFlush(student);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端收到消息：" + byteBuf.toString(CharsetUtil.UTF_8));
    }
}
