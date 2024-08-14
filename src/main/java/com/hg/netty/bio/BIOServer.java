package com.hg.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description
 * @Author ygl
 * @Create 2024/8/14 09:46
 */
public class BIOServer {

    public static void main(String[] args) throws Exception {
        // 线程池机制
        // 1、创建一个线程池
        // 2、如果有客户端连接，就创建一个线程，与之通讯
        ExecutorService executor = Executors.newCachedThreadPool();
        // 创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(18888);
        System.out.println("服务器启动...");
        while (true) {
            System.out.println("线程信息id:" + Thread.currentThread().getId() + " 名字: " + Thread.currentThread().getName());
            // 监听，等待客户端连接
            System.out.println("等待连接...");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");
            // 创建一个线程，与之通信
            executor.execute(() -> {
                handler(socket);
            });
        }

    }

    /**
     * 与客户端通信的方法
     */
    public static void handler(Socket socket) {
        try {
            System.out.println("线程信息id:" + Thread.currentThread().getId() + " 名字: " + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            // 通过socket获取输入流
            InputStream inputStream = socket.getInputStream();
            // 循环读取客户端发送的数据
            while (true) {
                System.out.println("read...");
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println("[content]: " + new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
