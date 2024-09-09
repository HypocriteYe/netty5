package com.hg.netty.nio.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

public class OldIOClient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 17001);
        String fileName = "IMG_0133.JPG";
        FileInputStream inputStream = new FileInputStream(fileName);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        byte[] bytes = new byte[4096];
        long readCount;
        long total = 0;
        long startTime = System.currentTimeMillis();
        while ((readCount = inputStream.read(bytes)) > 0) {
            total += readCount;
            dataOutputStream.write(bytes);
        }
        System.out.println("发送总字节数：" + total + "耗时：" + (System.currentTimeMillis() - startTime));
        dataOutputStream.close();
        inputStream.close();
        socket.close();
    }
}
