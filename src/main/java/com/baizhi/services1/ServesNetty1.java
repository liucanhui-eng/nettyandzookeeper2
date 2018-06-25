package com.baizhi.services1;

import com.baizhi.util.InitUtil;

public class ServesNetty1 {
    public static void main(String[] args) throws Exception {
       new InitUtil().init(new ServerChannelInitializer1(),8990,"service1");
    }
}
