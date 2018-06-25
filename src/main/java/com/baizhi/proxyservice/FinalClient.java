package com.baizhi.proxyservice;

import com.baizhi.proxyservice.service.AddService;
import com.baizhi.rpc.HostAndPort;
import com.baizhi.rpc.RPCProxy;
import org.I0Itec.zkclient.ZkClient;

import java.util.ArrayList;
import java.util.List;

public class FinalClient {
    public static void main(String[] args) throws Exception {
        List<HostAndPort> hostAndPorts = getHostAndPorts();
        RPCProxy<AddService> proxy = new RPCProxy();
        proxy.setRpcClient(new RpcClientImpl());
        proxy.setHostAndPorts(hostAndPorts);
        AddService addService = proxy.createPorxy(AddService.class);
        Integer add = addService.add(4, 8);
        System.out.println("结果O(∩_∩)O " + add);

    }
    public static List<HostAndPort> getHostAndPorts() {
        ZkClient zkClient = new ZkClient("192.168.152.128:2181");
        List<String> children = zkClient.getChildren("/services");
        ArrayList<HostAndPort> hostAndPorts = new ArrayList<>();
        for (String child : children) {
            HostAndPort hostAndPort = (HostAndPort) zkClient.readData("/services/" + child);
            hostAndPorts.add(hostAndPort);
        }
        return hostAndPorts;
    }
}
