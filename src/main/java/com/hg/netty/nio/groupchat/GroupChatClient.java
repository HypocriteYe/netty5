package com.hg.netty.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GroupChatClient {

    private final String HOST = "127.0.0.1";
    private final int PORT = 16667;
    private String username;

    private Selector selector;
    private SocketChannel socketChannel;

    public GroupChatClient() throws IOException {
        this.selector = Selector.open();
        this.socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        System.out.println(socketChannel.getLocalAddress().toString().substring(1) + " is ok...");
        username = socketChannel.getLocalAddress().toString().substring(1);
    }

    // 向服务端发送消息
    private void sendMessage(String message) {
        message = username + ": " + message;
        try {
            socketChannel.write(ByteBuffer.wrap(message.getBytes()));
        } catch (IOException e) {
        }
    }

    // 从服务端读取消息
    // TODO 解决客户端收到第一个消息后，无法收到后续消息的问题
    private void readMessage() {
        try {
            int count = selector.select();
            if (count > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(100);

                        while (socketChannel.isOpen()) {
                            byteBuffer.clear();
                            int read = socketChannel.read(byteBuffer);
                            if (read > 0) {
                                byteBuffer.flip();
                                byte[] messageBytes = new byte[byteBuffer.limit()];
                                byteBuffer.get(messageBytes);
                                String message = new String(messageBytes).trim();
                                System.out.println(message);
                            } else if (read == -1) {
                                socketChannel.close();
                                selectionKey.cancel();
                                System.out.println("对方关闭了连接...");
                                break;
                            }
                        }

                    }
//                    iterator.remove();
                }
            } else {
//                System.out.println("没有可用的通道...");
            }
        } catch (IOException ignored) {}
    }

    private void readMessages() {
        while (true) {
            try {
                int count = selector.select();
                if (count > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();

                        if (selectionKey.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
                            int read = socketChannel.read(byteBuffer);
                            if (read > 0) {
                                byteBuffer.flip();
                                byte[] messageBytes = new byte[byteBuffer.limit()];
                                byteBuffer.get(messageBytes);
                                String message = new String(messageBytes).trim();
                                System.out.println(message);
                            } else if (read == -1) {
                                socketChannel.close();
                                selectionKey.cancel();
                                System.out.println("对方关闭了连接...");
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //读取从服务器端回复的消息
    public void readInfo() {
        try {
            int readChannels = selector.select();
            if (readChannels > 0) {//有可以用的通道
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        //得到相关的通道
                        SocketChannel sc = (SocketChannel) key.channel();
                        //得到一个 Buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        //读取
                        sc.read(buffer);
                        //把读到的缓冲区的数据转成字符串
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                }
                iterator.remove(); //删除当前的 selectionKey,防止重复操作
            } else {
                //System.out.println("没有可以用的通道...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        GroupChatClient client = new GroupChatClient();

        new Thread(() -> {
            while (true) {
                client.readMessage();
//                client.readInfo();
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                }
            }
        }).start();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            client.sendMessage(s);
        }
    }
}
