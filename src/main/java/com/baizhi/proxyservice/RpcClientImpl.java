package com.baizhi.proxyservice;

import com.baizhi.rpc.HostAndPort;
import com.baizhi.rpc.MethodInvokeMeta;
import com.baizhi.rpc.Result;
import com.baizhi.util.ObjectMessageToMessageDecoder;
import com.baizhi.util.ObjectMessageToMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class RpcClientImpl implements RpcClient {

    @Override
    public Result call(MethodInvokeMeta mim, HostAndPort hap) {
        System.out.println("min  "+mim+"\n"+"hostandpost   "+hap);
        final MethodInvokeMeta min = mim;
        //创建起动引导
        Bootstrap bootstrap = new Bootstrap();
        //创建线程池组
        EventLoopGroup worker = new NioEventLoopGroup();
        //配置线程池组
        bootstrap.group(worker);
        //设置服务器实现
        bootstrap.channel(NioSocketChannel.class);
        //接收返回结果
        final Result result = new Result();
        //初始化信道配置

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {

                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new LengthFieldPrepender(2));
                pipeline.addLast(new ObjectMessageToMessageEncoder());
                pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                pipeline.addLast(new ObjectMessageToMessageDecoder());
                pipeline.addLast(new ChannelHandlerAdapter() {
                    @Override
                    //捕获异常
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        System.out.println("出错了:" + cause.getMessage());
                    }
                    @Override
                    //向服务器发送请求
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        ChannelFuture channelFuture = ctx.writeAndFlush(min);
                        ctx.writeAndFlush(min);
                        channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                        channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        //接收数据
                        Result result1 = (Result) msg;
                        result.setException(result1.getException());
                        result.setReturnValue(result1.getReturnValue());
                        System.out.println("RpcClientImpl  "+result);
                    }
                });
            }

        });
        //绑定端口启动服务
        try {
            System.out.println("端口"+hap.getPort());
            ChannelFuture channelFuture = bootstrap.connect("localhost", hap.getPort()).sync();
            //关闭socketChannel
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("处理完毕");
        //关闭线程资源
        worker.shutdownGracefully();
        System.out.println("RpcClientImpl 返回处理结果");
        return result;
    }
}
