package com.hg.netty.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;
import java.util.UUID;


public class MyTcpServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol msg) throws Exception {
        byte[] content = msg.getContent();
        String s = new String(content, CharsetUtil.UTF_8);
        System.out.println("服务端接收到的消息长度: " + msg.getLen());
        System.out.println("服务端接收到消息: " + s);
        System.out.println("服务端接收到消息数量: " + ++count);
        // 服务端给客户端返回随机变量
        String uuid = UUID.randomUUID().toString();
        MessageProtocol messageProtocol = new MessageProtocol(uuid.length(), uuid.getBytes(StandardCharsets.UTF_8));
        channelHandlerContext.writeAndFlush(messageProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
