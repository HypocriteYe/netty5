package com.hg.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 自定义handler过滤器
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server 线程信息：" + Thread.currentThread().getName());
        System.out.println("server context = " + ctx);
        System.out.println("channel和pipeline的关系");
        Channel channel = ctx.channel();
        // 本质是一个双向链表
        ChannelPipeline pipeline = ctx.pipeline();
        // 这个buffer是netty的，不是nio的
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端发送的消息是：" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + channel.remoteAddress().toString().substring(1));

        // 当有耗时任务时，将任务提交到channel相关的taskQueue中
        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                ctx.writeAndFlush(Unpooled.copiedBuffer("channelRead: hello,客户端~", CharsetUtil.UTF_8));
            } catch (Exception e) {
                System.out.println("执行异常: " + e.getMessage());
            }
        });

        // 用户自定义定时任务
        // ctx.channel().eventLoop().schedule(() -> {
        //     System.out.println("timer task");
        // }, 5, TimeUnit.SECONDS);
        System.out.println("go on...");
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
