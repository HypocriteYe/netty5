package com.hg.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class MyServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {
            String eventType = "";
            switch (event.state()) {
                case READER_IDLE -> eventType = "读空闲";
                case WRITER_IDLE -> eventType = "写空闲";
                case ALL_IDLE -> eventType = "读写空闲";
                default -> System.out.println("未知事件");
            }
            System.out.println(ctx.channel().remoteAddress().toString().substring(1 ) + "超时事件: " + eventType);
            // 如果发生空闲，可以关闭通道
            ctx.channel().close();
        }
    }
}
