package com.hg.netty.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @description
 * @Author ygl
 * @Create 2024/8/14 10:03
 */
public class BIOClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 18888));
        socket.getOutputStream().write("hello world".getBytes());
    }
}
