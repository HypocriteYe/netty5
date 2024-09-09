package com.hg.netty.codec2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

public class NettyClientHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    private static Random random = new Random();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyDataInfo.MyMessage myMessage) throws Exception {
        System.out.println("客户端接收到消息：" + myMessage);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int i = random.nextInt(3);
        MyDataInfo.MyMessage myMessage = null;
        if (i == 0) {
            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.StudentType)
                    .setStudent(MyDataInfo.Student.newBuilder().setId(4).setName("玉麒麟 卢俊义").build())
                    .build();
        } else {
            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.WorkerType)
                    .setWorker(MyDataInfo.worker.newBuilder().setAge(20).setName("老李").build())
                    .build();
        }
        ctx.writeAndFlush(myMessage);
    }
}
