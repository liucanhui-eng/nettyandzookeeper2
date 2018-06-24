package com.baizhi.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.commons.lang3.SerializationUtils;

import java.util.List;

public class ObjectMessageToMessageDecoder extends MessageToMessageDecoder<ByteBuf> {


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        /*
         * @Param byteBuffer 序列化以后的对象
         * @Param list 数据帧
         * */
        byte[] bytes=new byte[byteBuf.readableBytes()];
        //将byteBuf中的数据写入bytes
        byteBuf.readBytes(bytes);
        Object o = SerializationUtils.deserialize(bytes);
        System.out.println("解码解完后的对象  "+o);
        list.add(o);
    }
}
