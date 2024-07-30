package com.kjr21362.register;

import java.net.InetSocketAddress;

public interface ServiceRegistry {

    public void registerService(String remoteServiceName, InetSocketAddress address);

    public InetSocketAddress lookUpService(String remoteServiceName);
}
