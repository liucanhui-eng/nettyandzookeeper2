package com.baizhi.services2;

import com.baizhi.util.InitUtil;

public class ServesNetty2 {
    public static void main(String[] args) throws Exception {
        new InitUtil().init(new ServerChannelInitializer2(),8991,"service2");
    }
}
