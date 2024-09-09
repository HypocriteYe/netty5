package com.hg.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;
    // 返回的结果
    private String result;
    // 客户端调用方法时，传入的参数
    private String param;

    public void setParam(String param) {
        this.param = param;
    }

    /**
     * 被代理对象调用，发送数据给服务器
     * wait等待被channelRead唤醒
     * @return
     * @throws Exception
     */
    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(param);
        wait();
        return result;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 因为会在其他方法用到ctx
        this.context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.out.println("发生异常: " + cause.getMessage());
        ctx.close();
    }
}
