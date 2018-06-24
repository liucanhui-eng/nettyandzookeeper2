package com.baizhi.rpc;

import com.baizhi.proxyservice.RpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Random;

public class RPCProxy<T> implements InvocationHandler {
    Object subject;
    private List<HostAndPort> hostAndPorts;//支持动态更新

    public RPCProxy() {
    }

    public RPCProxy(Object subject) {
        this.subject = subject;
    }

    RpcClient rpcClient=null;


    public RpcClient getRpcClient() {
        return rpcClient;
    }

    public void setRpcClient(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public void setHostAndPorts(List<HostAndPort> hostAndPorts) {
        this.hostAndPorts = hostAndPorts;
    }



    Class targetInterface;
    public T createPorxy(Class targetInterface){
        this.targetInterface=targetInterface;
        return (T) Proxy.newProxyInstance(RPCProxy.class.getClassLoader(),new Class[]{targetInterface},this);
    }
    public Object invoke(Object proxy, Method method, Object[] args)  {
        MethodInvokeMeta mim=new MethodInvokeMeta();
        mim.setTargetInterface(targetInterface);
        mim.setArgs(args);
        mim.setMethod(method.getName());
        mim.setParameterTypes(method.getParameterTypes());
        HostAndPort hostAndPort=null;//随机从hostAndPorts获取一个做远程调度
        int index = new Random().nextInt(hostAndPorts.size());
        System.out.println("可选服务器个数： "+hostAndPorts.size()+"\n"+"已选择的服务器  "+hostAndPorts.get(index).getPort());
        hostAndPort=hostAndPorts.get(index);
        Result result = rpcClient.call(mim, hostAndPort);
        System.out.println("代理类 min对象   "+mim);
        System.out.println("RPCProxy 应用服务器返回结果   "+result);
        return result.getReturnValue();
    }
}
