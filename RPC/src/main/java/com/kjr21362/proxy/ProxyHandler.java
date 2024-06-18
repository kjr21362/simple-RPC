package com.kjr21362.proxy;

import com.kjr21362.common.InvocationParams;
import com.kjr21362.protocol.HttpClient;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyHandler implements InvocationHandler {

    private final Class<?> service;

    public ProxyHandler(Class<?> service) {
        this.service = service;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        InvocationParams invocationParams = new InvocationParams(service.getName(),
            method.getName(), method.getParameterTypes(), args);

        HttpClient httpClient = new HttpClient();
        return httpClient.send("localhost", 8989, invocationParams);
    }
}
