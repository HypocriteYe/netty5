package com.hg.netty.nio;

import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @description
 * @Author ygl
 * @Create 2024/8/14 11:51
 */
public class BufferOperation {

    public static void main(String[] args) throws Exception {
//        putAndGet();
//        toReadOnly();
//        mappedByteBuffer();
        scatteringAndGathering();
    }

    public static void  putAndGet() {

        // 创建一个Buffer
        ByteBuffer buffer = ByteBuffer.allocate(64);
        // 类型化方式放入数据
        buffer.putInt(100);
        buffer.putLong(9);
        buffer.putChar('画');
        buffer.putShort((short) 4);

        buffer.flip();
        System.out.println();
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());
    }

    public static void toReadOnly() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(64);
            for (int i = 0; i < 64; i++) {
                buffer.put((byte) i);
            }
            buffer.flip();
            ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
            System.out.println(readOnlyBuffer.getClass());
            while (readOnlyBuffer.hasRemaining()) {
                System.out.println(readOnlyBuffer.get());
            }
            readOnlyBuffer.put((byte) 100);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void mappedByteBuffer() throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("./4.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 参数1：读写模式
         * 参数2：可以修改的起始位置
         * 参数3：映射到内存的字节数
         * MappedByteBuffer的实际类型是DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte) 'H');;
        mappedByteBuffer.put(3, (byte) '9');
        mappedByteBuffer.put(5, (byte) 'Y');
        channel.close();
        randomAccessFile.close();
        System.out.println("修改成功~~");

    }

    /**
     * scattering：将数据写入buffer时可以采用buffer数组
     * gathering：从buffer读取数据时，可以采用buffer数组
     */
    public static void scatteringAndGathering() throws Exception {
        // 使用 ServerSocketChannel 和 SocketChannel 网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(17000);
        // 绑定端口到 socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等待客户端连接（telnet）
        SocketChannel socketChannel = serverSocketChannel.accept();
        // 假定从客户端接收8个字节
        int messageLength = 8;
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                long l = socketChannel.read(byteBuffers);
                byteRead += l;
                System.out.println("byteRead = " + byteRead);
                // 使用流打印，看看当前这个buffer的position和limit
                Arrays.stream(byteBuffers).map(buffer -> "position = " + buffer.position() + ", limit = " + buffer.limit()).forEach(System.out::println);
            }
            // 将所有buffer进行flip
            Arrays.asList(byteBuffers).forEach(ByteBuffer::flip);
            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }

            // 将所有buffer进行clear
            Arrays.asList(byteBuffers).forEach(ByteBuffer::clear);
            System.out.println("byteRead = " + byteRead + ", byteWrite = " + byteWrite + ", messageLength = " + messageLength);
        }
    }
}
