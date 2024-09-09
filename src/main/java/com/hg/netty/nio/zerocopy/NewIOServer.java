package com.hg.netty.nio.zerocopy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NewIOServer {

    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(17001));
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            int readCount = 0;
            while (readCount != -1) {
                try {
                   readCount = socketChannel.read(buffer);
                } catch (Exception e) {}
                // 倒带 ，将position置为0,mark作废
                buffer.rewind();
            }
        }
    }
}
