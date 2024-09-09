package com.hg.netty.nio.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {

    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 17001));
        String fileName = "IMG_0133.JPG";
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        long startTime = System.currentTimeMillis();
        // linux下一次transferTo就可以完成传输
        // windows下一次transferTo只能发送8m，需要分段传输
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("发送总字节数：" + transferCount + ",耗时：" + (System.currentTimeMillis() - startTime));
    }
}
