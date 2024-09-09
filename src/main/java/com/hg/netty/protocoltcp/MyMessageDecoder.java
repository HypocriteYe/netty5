package com.hg.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyMessageDecoder extends ReplayingDecoder<MessageProtocol> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyMessageDecoder decode invoked");
        int len = in.readInt();
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        // 封装成messageProtocol对象，放到list中
        MessageProtocol msg = new MessageProtocol(len, bytes);
        out.add(msg);
    }
}
