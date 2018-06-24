package com.baizhi.client;


import com.baizhi.util.ObjectMessageToMessageDecoder;
import com.baizhi.util.ObjectMessageToMessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LengthFieldPrepender(2));
        pipeline.addLast(new ObjectMessageToMessageEncoder());


        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
        pipeline.addLast(new ObjectMessageToMessageDecoder());
        pipeline.addLast(new ClientChannelHandlerApater());

    }

}
