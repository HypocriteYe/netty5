/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.hg.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    // 充当业务线程池，可以将任务提交到该线程池中
    static final DefaultEventExecutorGroup group = new DefaultEventExecutorGroup(16);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // System.out.println("Server received: " + msg);
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String s = new String(bytes, CharsetUtil.UTF_8);
        System.out.println("Server received: " + s);
        ctx.writeAndFlush(Unpooled.copiedBuffer(s, CharsetUtil.UTF_8));

        // System.out.println("Server channelRead的线程名称: " + Thread.currentThread().getName());
        // // 将任务提交到线程池
        // group.submit(() -> {
        //     System.out.println("Server channelRead线程池的线程名称: " + Thread.currentThread().getName());
        //     ByteBuf buf = (ByteBuf) msg;
        //     byte[] bytes = new byte[buf.readableBytes()];
        //     String s = new String(bytes, CharsetUtil.UTF_8);
        //     ctx.writeAndFlush(s);
        // });
        System.out.println("go on...");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
