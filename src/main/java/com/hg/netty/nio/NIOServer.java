package com.hg.netty.nio;

import javax.sound.midi.Soundbank;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public static void main(String[] args) throws Exception {
        // 创建serverSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 创建selector对象
        Selector selector = Selector.open();
        // 绑定一个端口6666，在服务端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 把serverSocketChannel注册到selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }
            // 有事件发生
            // 获取到SelectionKey集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 获取到key
                if (selectionKey.isAcceptable()) {
                    // 获取到channel
                    try {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        // 注册到selector，关注事件为read，给socketChannel绑定一个buffer
                        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    } catch (Exception e) {
                    }
                }
                // 发生OP_READ
                if (selectionKey.isReadable()) {
                    try {
                        // 通过key，反向获取到对应channel
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                        channel.read(buffer);
                        System.out.println("从客户端读取数据: " + new String(buffer.array()));
                    } catch (Exception e) {
                    }
                }
                // 手动从集合中移除当前的selectionKey，防止重复操作
                iterator.remove();
            }

        }
    }
}
