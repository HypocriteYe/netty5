package com.hg.netty.handlerchain;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {


    /**
     * decode方法会根据接收到的数据，调用多次，直到确定没有新的元素被添加到list或byteBuf没有更多可读字节
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("MyByteToLongDecoder decode invoked");
        if (byteBuf.readableBytes() >= 8) {
            list.add(byteBuf.readLong());
        }
    }
}
