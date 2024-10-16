package com.hg.netty.multichat;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChannelGroupManager {
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static void addChannel(Channel channel) {
        channels.add(channel);
    }

    public static void removeChannel(Channel channel) {
        channels.remove(channel);
    }

    public static ChannelGroup getChannels() {
        return channels;
    }
}
