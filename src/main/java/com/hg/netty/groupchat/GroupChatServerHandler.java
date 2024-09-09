package com.hg.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    // 定义一个channel组，管理所有的channel
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    // 实现私聊
    // private static Map<String, Channel> channels = new ConcurrentHashMap<>();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Channel channel = channelHandlerContext.channel();
        channelGroup.forEach(ch -> {
            if (ch != channel) {
                ch.writeAndFlush("【客户：" + channel.remoteAddress().toString().substring(1) + "】发送消息：" + s + "\n");
            } else {
                ch.writeAndFlush("【自己】" + s + "\n");
            }
        });
    }

    // 一旦连接被确立，第一个执行的方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 将该客户端加入聊天的信息推送给其他客户端
        channelGroup.writeAndFlush("【系统消息】" + sdf.format(new Date()) +" 客户端：" + ctx.channel().remoteAddress().toString().substring(1) + " 加入聊天室\n");
        channelGroup.add(ctx.channel());
        // channels.put("id001", ctx.channel());
    }

    // 断开连接，将xx客户离开信息推送给当前在线客户端
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush("【系统消息】客户端：" + ctx.channel().remoteAddress().toString().substring(1) + " 离开聊天室\n");
    }

    // 表示处于活动状态，提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress().toString().substring(1) + " 上线了~");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress().toString().substring(1) + " 离线了~");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.close();
    }
}
