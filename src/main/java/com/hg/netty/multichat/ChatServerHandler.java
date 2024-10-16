package com.hg.netty.multichat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
    private final String serverId;

    public ChatServerHandler(String serverId) {
        this.serverId = serverId;  // 当前服务器的唯一标识
    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("Received message from " + incoming.remoteAddress() + ": " + msg);

        // 将消息广播给当前服务器的所有客户端
        for (Channel client : ChannelGroupManager.getChannels()) {
            if (client != incoming) {
                client.writeAndFlush("[Server Broadcast] " + msg + "\n");
            }
        }

        // 如果服务器能够已知其他服务器的情况，直接由当前服务器进行转发，否则通过特殊的客户端进行转发到其他服务器
        // 将消息转发给其他服务器，附加上当前服务器的 serverId
        // 从 ServerConnectionManager 获取其他服务器的连接
        for (Map.Entry<String, Channel> entry : ServerConnectionManager.getServerConnections().entrySet()) {
            String otherServerId = entry.getKey();
            Channel otherServerChannel = entry.getValue();

            // 如果不是当前服务器，将消息转发给其他服务器
            if (!otherServerId.equals(serverId)) {
                String messageWithServerId = serverId + ":" + msg;
                otherServerChannel.writeAndFlush(messageWithServerId + "\n");
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 可以注册所有服务器地址
        // ServerConnectionManager.addServerConnection(new NioSocketChannel());
    }
}
