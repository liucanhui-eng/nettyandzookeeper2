package com.baizhi.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientNetty {
    public static void main(String[] args) throws Exception {
        //创建起动引导
        Bootstrap bootstrap = new Bootstrap();
        //创建线程池组
        EventLoopGroup worker=new NioEventLoopGroup();
        //配置线程池组
        bootstrap.group(worker);
        //设置服务器实现
        bootstrap.channel(NioSocketChannel.class);
        //初始化信道配置
        bootstrap.handler(new ClientChannelInitializer());
        //绑定端口启动服务
        ChannelFuture channelFuture = bootstrap.connect("localhost", 8989).sync();
        //关闭socketChannel
        channelFuture.channel().closeFuture().sync();
        //关闭线程资源
        worker.shutdownGracefully();
    }
}
