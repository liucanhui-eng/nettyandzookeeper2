package com.baizhi.services1;

import com.baizhi.rpc.MethodInvokeMeta;
import com.baizhi.rpc.Result;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;

public class   ServerChannelHandlerApater1 extends ChannelHandlerAdapter {
    @Override
    //捕获错误方法
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("出错了 " + cause.getMessage());
        cause.printStackTrace();
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(" ServerChannelHandlerApater2 接收到的对象 \t" + msg);
        MethodInvokeMeta meta = null;
        Result result = new Result();
        if (msg instanceof MethodInvokeMeta) {
            meta = (MethodInvokeMeta) msg;
            if (meta != null) {
                try {
                    Class<?> targetInterface = meta.getTargetInterface();
                    Method method = targetInterface.getMethod(meta.getMethod(), meta.getParameterTypes());
                    Object invoke = method.invoke(new AddServiceImpl1(), meta.getArgs());
                    result.setReturnValue(invoke);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    result.setException(e);
                }
            }
        }


        System.out.println("接收成功"+result);
        //向服务端发相应
        ChannelFuture channelFuture = ctx.writeAndFlush(result);
        //监听序列化异常
        channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        //发生异常时关闭连接
        channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

        //关闭通道
       // if (msg instanceof Close) {
            System.out.println("关闭====服务器");
            channelFuture.addListener(ChannelFutureListener.CLOSE);
      //  }
    }


}
