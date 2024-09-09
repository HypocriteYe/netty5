package com.hg.netty.handlerchain;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 可以代替MyByteToLongDecoder
 */
public class MyReplayDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyReplayDecoder is invoked");
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
