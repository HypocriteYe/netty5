package com.hg.netty.dubborpc.consumer;

import com.hg.netty.dubborpc.HelloService;
import com.hg.netty.dubborpc.netty.NettyClient;

import java.util.concurrent.TimeUnit;

public class RPCClient {

    private static final String PROVIDER_NAME = "HelloService#hello#";

    public static void main(String[] args) throws Exception {
        NettyClient client = new NettyClient();
        HelloService service = (HelloService) client.getBean(HelloService.class, PROVIDER_NAME);
        // 通过代理对象调用服务提供者的方法
       for (;;) {
           TimeUnit.SECONDS.sleep(2);
           String result = service.hello("111");
           System.out.println("调用结果: " + result);
       }
    }
}
