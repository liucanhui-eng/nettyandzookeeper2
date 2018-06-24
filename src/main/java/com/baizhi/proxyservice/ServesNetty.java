package com.baizhi.proxyservice;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServesNetty {
    public static void main(String[] args) throws Exception {
        //创建启动引导
        ServerBootstrap bootstrap = new ServerBootstrap();
        //创建线程池组
        EventLoopGroup worker=new NioEventLoopGroup();
        EventLoopGroup boss=new NioEventLoopGroup();
        //配置线程池组
        bootstrap.group(boss,worker);
//        bootstrap.group(worker);
        //设置服务器实现
        bootstrap.channel(NioServerSocketChannel.class);
        //初始化信道配置
        bootstrap.childHandler(new ServerChannelInitializer());
        //绑定IP端口
        System.out.println("服务器已启动");
        ChannelFuture channelFuture = bootstrap.bind(8989).sync();
        //关闭资源
        channelFuture.channel().closeFuture().sync();
        boss.shutdownGracefully();
        worker.shutdownGracefully();

    }
}
