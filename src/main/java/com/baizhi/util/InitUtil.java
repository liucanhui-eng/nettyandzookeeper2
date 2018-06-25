package com.baizhi.util;

import com.baizhi.rpc.HostAndPort;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

public class InitUtil {

    public  void init(ChannelInitializer channelInitializer,String host,int port)throws Exception{
        //创建起动引导
        Bootstrap bootstrap = new Bootstrap();
        //创建线程池组
        EventLoopGroup worker=new NioEventLoopGroup();
        //配置线程池组
        bootstrap.group(worker);
        //设置服务器实现
        bootstrap.channel(NioSocketChannel.class);
        //初始化信道配置
        bootstrap.handler(channelInitializer);
        //绑定端口启动服务
        ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
        //关闭socketChannel
        channelFuture.channel().closeFuture().sync();
        //关闭线程资源
        worker.shutdownGracefully();
    }
    public  void init(ChannelInitializer channelInitializer,int port,String znodeName)throws Exception{
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
        bootstrap.childHandler(channelInitializer);
        //绑定IP端口
        System.out.println("服务器已启动");
        ChannelFuture channelFuture = bootstrap.bind(port).sync();
        ZkClient client = new ZkClient("192.168.152.128:2181");
        String s = client.create("/services/"+znodeName,new HostAndPort("localsost",port), CreateMode.EPHEMERAL);
        //关闭资源
        channelFuture.channel().closeFuture().sync();
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }
}
