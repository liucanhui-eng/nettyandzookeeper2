package com.baizhi.client;


import com.baizhi.util.InitUtil;

public class ClientNetty {
    public static void main(String[] args) throws Exception {
        new InitUtil().init(new ClientChannelInitializer(),"localhost",8989);
    }
}
