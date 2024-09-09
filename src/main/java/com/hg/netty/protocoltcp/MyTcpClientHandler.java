package com.hg.netty.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class MyTcpClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {
        System.out.println("MyTcpClientHandler channelRead0 invoked");
        System.out.println("MyTcpClientHandler receive server message:" + new String(messageProtocol.getContent(), Charset.defaultCharset()));
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 5; i++) {
            String message = "今天天气冷，吃火锅";
            byte[] content = message.getBytes(Charset.defaultCharset());
            int len = content.length;
            MessageProtocol msg = new MessageProtocol();
            msg.setLen(len);
            msg.setContent(content);
            ctx.writeAndFlush(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
