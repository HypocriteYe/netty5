package com.hg.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * TextWebSocketFrame表示文本帧
 */
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        System.out.println("服务器收到消息：" + textWebSocketFrame.text());
        channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame("服务器时间: " + LocalDateTime.now() + " " + textWebSocketFrame.text()));
    }

    /**
     * 客户端连接后，触发该方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded被调用: " + ctx.channel().id().asLongText());
        System.out.println("handlerAdded被调用: " + ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved被调用: " + ctx.channel().id().asLongText());
        System.out.println("handlerRemoved被调用: " + ctx.channel().id().asShortText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生: " + cause);
    }
}
