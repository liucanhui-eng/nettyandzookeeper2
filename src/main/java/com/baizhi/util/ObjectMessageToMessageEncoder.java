package com.baizhi.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.List;

public class ObjectMessageToMessageEncoder extends MessageToMessageEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {
        /*
        * @Param o 要编码的对象
        * @Param list数据帧
        * */
        System.out.println("编码 编码前的对象 "+ o);
        //容器
        ByteBuf buffer = channelHandlerContext.alloc().buffer();
        //序列化对象
        byte[] bytes = SerializationUtils.serialize((Serializable) o);
        //打包数据
        ByteBuf buf = buffer.writeBytes(bytes);
        list.add(buf);
    }
}
