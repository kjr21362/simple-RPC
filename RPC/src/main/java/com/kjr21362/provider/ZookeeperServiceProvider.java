package com.kjr21362.provider;

import com.kjr21362.config.ServiceConfig;
import com.kjr21362.register.ZookeeperRegistry;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZookeeperServiceProvider implements ServiceProvider{

    private static ZookeeperServiceProvider instance = null;

    private final ZookeeperRegistry zookeeperRegistry;
    private final Map<String, Object> serviceMap;
    private static final Object LOCK = new Object();

    private ZookeeperServiceProvider(){
        serviceMap = new ConcurrentHashMap<>();
        zookeeperRegistry = new ZookeeperRegistry();
    }

    @Override
    public void registerService(ServiceConfig serviceConfig) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            zookeeperRegistry.registerService(serviceConfig.getServiceName(), new InetSocketAddress(host, 8989));

            serviceMap.put(serviceConfig.getServiceName(), serviceConfig.getService());

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getService(String remoteServiceName) {
        return serviceMap.get(remoteServiceName);
    }

    public static ZookeeperServiceProvider getInstance(){
        if(instance != null){
            return instance;
        }

        synchronized (LOCK){
            if(instance != null){
                return instance;
            }

            instance = new ZookeeperServiceProvider();
            return instance;
        }
    }
}
