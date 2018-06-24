package com.baizhi.services2;

import com.baizhi.rpc.HostAndPort;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

public class ServesNetty2 {
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
        bootstrap.childHandler(new ServerChannelInitializer2());
        //绑定IP端口
        System.out.println("服务器已启动");
        ChannelFuture channelFuture = bootstrap.bind(8991).sync();
        ZkClient client = new ZkClient("192.168.152.128:2181");
        String s = client.create("/services/service2",new HostAndPort("localsost",8991), CreateMode.EPHEMERAL);
        //关闭资源
        channelFuture.channel().closeFuture().sync();
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }
}
