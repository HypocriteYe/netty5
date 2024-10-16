package com.hg.netty.multichat;

import java.util.concurrent.TimeUnit;

public class ServerBootStrap {

    public static void main(String[] args) throws Exception {
        new ChatServer(8080, "ServerA").start();
        TimeUnit.SECONDS.sleep(10);
        new ChatClient("localhost", 9090, "ServerA");
    }
}
