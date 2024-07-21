package com.kjr21362;

import com.kjr21362.protocol.NettyServer;
import com.kjr21362.register.LocalRegister;

public class Provider {
    public static void main(String[] args) {

        LocalRegister.register(HelloService.class.getName(), HelloServiceImpl.class);

        // BIO socket server
        //HttpServer httpServer = new HttpServer(8989);
        //httpServer.run();

        // Netty server
        NettyServer nettyServer = new NettyServer(8989);
        nettyServer.run();
    }
}