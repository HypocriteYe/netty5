package com.hg.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class NettyBuffer {

    public static void main(String[] args) {
        copiedBuffer();
    }

    public static void byteBuffer() {
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        System.out.println("capacity = " + buffer.capacity());
        for (int i = 0; i < 10; i++) {
            System.out.println(buffer.getByte(i));
            System.out.println(buffer.readByte());
        }
    }

    public static void copiedBuffer() {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world", Charset.forName("utf-8"));
        if (byteBuf.hasArray()) {
            byte[] array = byteBuf.array();
            // 将array转成字符串
            System.out.println(new String(array, Charset.forName("utf-8")));
            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());
            // 可读取的字节数
            System.out.println(byteBuf.readableBytes());
            System.out.println(byteBuf.getCharSequence(4, 6, StandardCharsets.UTF_8));
        }
    }
}
