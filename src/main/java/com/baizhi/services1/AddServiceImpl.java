package com.baizhi.services1;

import com.baizhi.proxyservice.service.AddService;

public class AddServiceImpl implements AddService {
    @Override
    public Integer add(Integer a, Integer b) {
        return a+b;
    }
}
