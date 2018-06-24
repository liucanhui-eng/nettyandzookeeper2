package com.baizhi.services2;

import com.baizhi.proxyservice.service.AddService;

public class AddServiceImpl2 implements AddService {
    @Override
    public Integer add(Integer a, Integer b) {
        return a*b;
    }
}
