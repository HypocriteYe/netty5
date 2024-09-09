package com.hg.netty.dubborpc.provider.impl;

import com.hg.netty.dubborpc.HelloService;

public class HelloServiceImpl implements HelloService {

    private int count = 0;

    @Override
    public String hello(String message) {
        System.out.println("HelloServiceImpl收到：" + message  + "第" + ++count + "次");
        // return "HelloServiceImpl: " + message;
        return switch (message) {
            case "你好" -> "你好，客户端" + "第" + count + "次";
            case "111" -> "你好，客户端->1111" + "第" + count + "次";
            default -> "非法参数" + "第" + count + "次";
        };
    }
}
