package com.kjr21362.provider;

import com.kjr21362.config.ServiceConfig;

public interface ServiceProvider {

    void registerService(ServiceConfig serviceConfig);

    Object getService(String remoteServiceName);
}
