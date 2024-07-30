package com.kjr21362;

import com.kjr21362.config.ServiceConfig;
import com.kjr21362.protocol.NettyServer;
import com.kjr21362.provider.ZookeeperServiceProvider;
import com.kjr21362.register.ZookeeperRegistry;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class Provider {
    public static void main(String[] args) {

        //LocalRegistry.register(HelloService.class.getName(), HelloServiceImpl.class);

        // BIO socket server
        //HttpServer httpServer = new HttpServer(8989);
        //httpServer.run();

        ZookeeperRegistry zookeeperRegistry = new ZookeeperRegistry();
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            zookeeperRegistry.registerService(HelloService.class.getName(), new InetSocketAddress(host, 8989));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        ZookeeperServiceProvider zookeeperServiceProvider = ZookeeperServiceProvider.getInstance();
        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setService(new HelloServiceImpl());
        zookeeperServiceProvider.registerService(serviceConfig);

        // Netty server
        NettyServer nettyServer = new NettyServer(8989);
        nettyServer.run();
    }
}