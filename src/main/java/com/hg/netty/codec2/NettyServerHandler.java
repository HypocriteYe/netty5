package com.hg.netty.codec2;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 自定义handler过滤器
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 读取从客户端发送的StudentPOJO.Student对象
        MyDataInfo.MyMessage myMessage = (MyDataInfo.MyMessage) msg;
        System.out.println("客户端发送的数据: " + myMessage.toString());
    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入到缓存并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("channelReadComplete: hello,客户端~", CharsetUtil.UTF_8));

    }

    /**
     * 处理异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.channel().close();
    }


}
