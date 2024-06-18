package com.kjr21362;

import com.kjr21362.proxy.ProxyInstanceFactory;

public class Consumer {
    public static void main(String[] args) {

        HelloService service = (HelloService) ProxyInstanceFactory.getInstance(HelloService.class);

        System.out.println(service.hello("callee"));
    }
}