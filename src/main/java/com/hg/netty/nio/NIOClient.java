package com.hg.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {

    public static void main(String[] args) throws Exception {
        // 获得一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        // 提供服务端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        // 连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("连接需要时间，客户端不会阻塞");
            }
        }
        // 如果连接成功，就发送数据
        String str = "hello world!";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        socketChannel.write(buffer);
        System.in.read();
    }
}
