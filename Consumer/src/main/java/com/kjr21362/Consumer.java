package com.kjr21362;

import com.kjr21362.common.InvocationParams;
import com.kjr21362.protocol.HttpClient;

public class Consumer {
    public static void main(String[] args) {

        InvocationParams invocationParams = new InvocationParams(HelloService.class.getName(),
            "hello", new Class[]{String.class}, new Object[]{"caller"});

        HttpClient httpClient = new HttpClient();
        Object response = httpClient.send("localhost", 8989, invocationParams);
        System.out.println(response);
    }
}