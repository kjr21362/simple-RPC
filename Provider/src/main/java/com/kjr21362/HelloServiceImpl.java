package com.kjr21362;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello " + name;
    }

    @Override
    public int add(int a, int b){
        return a + b;
    }
}
