package com.baizhi.client;

import com.baizhi.entity.Close;
import com.baizhi.proxyservice.service.AddService;
import com.baizhi.rpc.MethodInvokeMeta;
import com.baizhi.rpc.Result;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.PrintStream;

public class ClientChannelHandlerApater extends ChannelHandlerAdapter {
    @Override
    //捕获异常
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("出错了:" + cause.getMessage());
        File file = new File("F:/errorLog/log1.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        cause.printStackTrace(new PrintStream(file));
    }

    @Override
    //向服务器发送请求
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MethodInvokeMeta meta = new MethodInvokeMeta();
        meta.setTargetInterface(AddService.class);
        meta.setMethod("add");
        meta.setParameterTypes(new Class[]{Integer.class, Integer.class});
        meta.setArgs(new Integer[]{2, 5});
        ctx.writeAndFlush(meta);
        ctx.writeAndFlush(meta);
        ChannelFuture channelFuture = ctx.writeAndFlush(new Close());
        System.out.println("客户端向服务器发送请求 ，请求数据：" + meta);
        channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收数据
        if (msg instanceof Result) {
            Result result = (Result) msg;
            if (result.getException() != null) {
                System.out.println("出错了： " + result.getException());
            } else if (result.getReturnValue() != null) {
                System.out.println("结果为:  " + result.getReturnValue());
            }
        }
    }
}
