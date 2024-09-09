package com.hg.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * SimpleChannelInboundHandler是ChannelInboundHandlerAdapter的子类
 * HttpObject表示客户端和服务器端相互通讯的数据类型
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        if (httpObject instanceof HttpRequest httpRequest) {
            System.out.println("pipeline hashcode: " + channelHandlerContext.pipeline().hashCode());
            System.out.println("handler hashCode: " + this.hashCode());
            System.out.println("msg的类型: " + httpObject.getClass());
            System.out.println("客户端地址: " + channelHandlerContext.channel().remoteAddress().toString().substring(1));
            ByteBuf content = Unpooled.copiedBuffer("hello,我是服务器~", CharsetUtil.UTF_8);

            // 根据uri过滤指定资源
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico, 不做响应");
                return;
            }

            FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            httpResponse.headers().set(HttpHeaderNames.CONTENT_ENCODING, "UTF-8");
            // 将构建好的response返回
            channelHandlerContext.writeAndFlush(httpResponse);
        }
    }
}
