package com.hg.netty.multichat;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

public class ServerConnectionManager {
    private static final Map<String, Channel> serverConnections = new HashMap<>();

    public static void addServerConnection(String serverId, Channel channel) {
        serverConnections.put(serverId, channel);
    }

    public static void removeServerConnection(Channel channel) {
        serverConnections.remove(channel);
    }

    public static Map<String, Channel> getServerConnections() {
        return serverConnections;
    }
}
