package com.baizhi.proxyservice;

import com.baizhi.rpc.HostAndPort;
import com.baizhi.rpc.MethodInvokeMeta;
import com.baizhi.rpc.Result;

public interface RpcClient {
    public Result call(MethodInvokeMeta mim, HostAndPort hap);
}
