package com.baizhi.rpc;

import java.io.Serializable;

public class HostAndPort implements Serializable {
    String host;
    int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public HostAndPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public HostAndPort() {

    }
}
