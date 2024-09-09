package com.hg.netty.dubborpc.provider;

import com.hg.netty.dubborpc.netty.NettyServer;

public class RPCServer {

    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1", 17000);
    }
}
