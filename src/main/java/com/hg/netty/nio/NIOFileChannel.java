package com.hg.netty.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @description
 * @Author ygl
 * @Create 2024/8/14 11:00
 */
public class NIOFileChannel {

    public static void main(String[] args) throws Exception {
//        writeFile();
//        readFile();
//        readAndWrite();
        transferFrom();
    }

    public static void writeFile() throws Exception {
        String str = "hello fileChannel";
        // 创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("./file01.txt");
        // 通过fileOutputStream获取对应的FileChannel
        // 这个fileChannel的实际类型是FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 创建一个缓冲区ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 将str放入byteBuffer
        byteBuffer.put(str.getBytes());
        byteBuffer.flip();

        // 将byteBuffer数据写到fileChannel
        int write = fileChannel.write(byteBuffer);
        System.out.println("write = " + write);
        fileChannel.close();
        fileOutputStream.close();
    }

    public static void readFile() throws Exception{
        File file = new File("./file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        // 通过fileInputStream获取对应的FileChannel 实际为FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();
        // 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        // 将通道的数据读取到buffer
        fileChannel.read(byteBuffer);

        // 将byteBuffer的字节数据转换成String
        System.out.println(new String(byteBuffer.array()));
        fileChannel.close();
        fileInputStream.close();
    }

    public static void readAndWrite() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("./1.txt");
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("./2.txt");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        while (true) {
            // 每次读取完后情况buffer
            byteBuffer.clear();
            int read = inputStreamChannel.read(byteBuffer);
            System.out.println("read = " + read);
            if (read == -1) break;
            byteBuffer.flip();
            outputStreamChannel.write(byteBuffer);
        }
        inputStreamChannel.close();
        outputStreamChannel.close();
        fileInputStream.close();
        fileOutputStream.close();
    }

    public static void transferFrom() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("./1.txt");
        FileChannel sourceChannel = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("./3.txt");
        FileChannel destChannel = fileOutputStream.getChannel();

        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

        sourceChannel.close();
        destChannel.close();
        fileInputStream.close();
        fileOutputStream.close();

    }
}
