package com.kjr21362.proxy;

import java.lang.reflect.Proxy;

public class ProxyInstanceFactory {

    private static Object proxyInstance = null;

    public static Object getInstance(final Class<?> service) {
        if (proxyInstance == null) {
            proxyInstance = Proxy.newProxyInstance(service.getClassLoader(), new Class[] {service}, new ProxyHandler(
                service));
        }
        return proxyInstance;
    }
}
