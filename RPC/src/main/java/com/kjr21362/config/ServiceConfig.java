package com.kjr21362.config;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServiceConfig {
    Object service;

    public String getServiceName(){
        return service.getClass().getInterfaces()[0].getCanonicalName();
    }
}
