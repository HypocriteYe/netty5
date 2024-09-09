package com.hg.netty.nio.groupchat;

import io.netty.util.NettyRuntime;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {

    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 16667;

    public GroupChatServer() {
        try {
            this.selector = Selector.open();
            this.listenChannel = ServerSocketChannel.open();
            // 绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            // 设置非阻塞模式
            listenChannel.configureBlocking(false);
            // 将该serverChannel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
        }
    }

    public void listen() {
        System.out.println("监听线程: " + Thread.currentThread().getName());
        try {
            while (true) {
                int count = selector.select(2000);
                if (count > 0) {
                    // 有事件发生
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress().toString().substring(1) + "上线了");
                        }
                        if (selectionKey.isReadable()) {
                            // 通道是可读状态
                            readData(selectionKey);
                        }
                        // 删除key，防止重复处理
                        iterator.remove();
                    }
                } else {
                    System.out.println("服务器等待了2秒，无连接");
                }
            }
        } catch (Exception e) {
        } finally {

        }
    }

    // 读取客户端数据
    private void readData(SelectionKey key) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(100);
            int count = socketChannel.read(buffer);
            if (count > 0) {
                String msg = new String(buffer.array());
                System.out.println("from 客户端：" + msg);
                sendInfoToOtherClient(msg, socketChannel);
            }
        } catch (IOException e) {
            try {
                System.out.println("客户端: " + socketChannel.getRemoteAddress().toString().substring(1) + " 离线了");
                // 取消注册，关闭通道
                key.cancel();
                socketChannel.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }


        }
    }

    // 转发消息给其他的客户端
    private void sendInfoToOtherClient(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息...");
        System.out.println("服务器转发消息给其他客户端线程: " + Thread.currentThread().getName());
        for (SelectionKey key : selector.keys()) {
            try (Channel channel = key.channel()) {
                if (channel instanceof SocketChannel dest && channel != self) {
                    ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                    dest.write(buffer);
                }
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer server = new GroupChatServer();
        server.listen();
    }
}
