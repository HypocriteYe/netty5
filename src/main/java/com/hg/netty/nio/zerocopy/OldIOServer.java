package com.hg.netty.nio.zerocopy;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

// 传统IO的服务器端
public class OldIOServer {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(17001);
        while (true) {
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            try {
                byte[] bytes = new byte[4096];
                while (true) {
                    int read = dataInputStream.read(bytes);
                    if (read == -1) break;
                    System.out.println("read=" + read);
                    System.out.println(bytes);
                }
            } catch (Exception e) {
            }
        }
    }
}
