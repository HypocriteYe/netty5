package com.hg.netty.handlerchain;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Long aLong, ByteBuf byteBuf) throws Exception {
        System.out.println("MyLongToByteEncoder encode invoked");
        System.out.println("msg: " + aLong);
        byteBuf.writeLong(aLong);
    }
}
