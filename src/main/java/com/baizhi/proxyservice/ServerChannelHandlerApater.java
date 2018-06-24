package com.baizhi.proxyservice;

import com.baizhi.entity.Close;
import com.baizhi.proxyservice.service.AddService;
import com.baizhi.rpc.HostAndPort;
import com.baizhi.rpc.MethodInvokeMeta;
import com.baizhi.rpc.RPCProxy;
import com.baizhi.rpc.Result;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.I0Itec.zkclient.ZkClient;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ServerChannelHandlerApater extends ChannelHandlerAdapter {
    @Override
    //捕获错误方法
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("出错了 " + cause.getMessage());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("代理服务器接收到了请求数据 \t" + msg);
        MethodInvokeMeta meta = null;
        Result result = new Result();
        ChannelFuture channelFuture = null;

        //      关闭通道
        if (msg instanceof Close) {
            System.out.println("关闭====通道");
            channelFuture = ctx.writeAndFlush("");
            addListener(channelFuture);
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        } else {
            if (msg instanceof MethodInvokeMeta) {
                meta = (MethodInvokeMeta) msg;
                if (meta != null) {
                    try {
                        //获取目标接口
                        Class<?> targetInterface = meta.getTargetInterface();
                        //获取代理对象
                        RPCProxy<AddService> proxy = new RPCProxy();
                        //代理中心
                        ZkClient client = new ZkClient("192.168.152.128:2181");
                        //获取已经注册的服务
                        List<String> children = client.getChildren("/services");
                        ArrayList<HostAndPort> hostAndPorts = new ArrayList<>();
                        for (String child : children) {
                            HostAndPort hostAndPort = (HostAndPort) client.readData("/services/" + child);
                            hostAndPorts.add(hostAndPort);
                        }
                        //完善代理类
                        proxy.setHostAndPorts(hostAndPorts);
                        proxy.setRpcClient(new RpcClientImpl());
                        //获取目标方法
                        Method method = targetInterface.getMethod(meta.getMethod(), meta.getParameterTypes());
                        //代理类
                        AddService addService = proxy.createPorxy(AddService.class);
                        //调用方法
                        Object invoke = method.invoke(addService, meta.getArgs());
                        result.setReturnValue(invoke);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("调用远程服务获取到的结果  " + result);
                    }
                }
                //向服务端发相应
                channelFuture = ctx.writeAndFlush(result);
                addListener(channelFuture);
            }
        }

    }


    public static void addListener(ChannelFuture channelFuture){
        //监听序列化异常
        channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        //发生异常时关闭连接
        channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }
}
