package com.kjr21362;

import com.kjr21362.protocol.HttpServer;
import com.kjr21362.register.LocalRegister;

public class Provider {
    public static void main(String[] args) {

        LocalRegister.register(HelloService.class.getName(), HelloServiceImpl.class);

        HttpServer httpServer = new HttpServer(8989);
        httpServer.run();
    }
}