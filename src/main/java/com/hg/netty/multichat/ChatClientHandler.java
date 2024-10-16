package com.hg.netty.multichat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChatClientHandler extends SimpleChannelInboundHandler<String> {

    private final String serverId;

    public ChatClientHandler(String serverId) {
        this.serverId = serverId;  // 当前服务器的唯一标识
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当客户端与服务器成功连接时，调用此方法
        Channel serverChannel = ctx.channel();
        System.out.println("Connected to another server: " + serverChannel.remoteAddress());

        // 将连接到的服务器 Channel 添加到 ServerConnectionManager
        ServerConnectionManager.addServerConnection(serverId, serverChannel);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 解析消息中的 serverId 和真正的消息内容
        String[] parts = msg.split(":", 2);
        String sourceServerId = parts[0];
        String actualMessage = parts[1];

        // 如果消息来源是当前服务器，直接返回，不再处理
        if (sourceServerId.equals(serverId)) {
            return;
        }

        System.out.println("Received message from server " + sourceServerId + ": " + actualMessage);

        // 将消息广播给当前服务器的所有客户端
        for (Channel client : ChannelGroupManager.getChannels()) {
            client.writeAndFlush("[Broadcast from server " + sourceServerId + "] " + actualMessage + "\n");
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
