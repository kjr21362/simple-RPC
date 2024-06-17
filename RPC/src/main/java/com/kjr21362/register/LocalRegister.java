package com.kjr21362.register;

import java.util.HashMap;
import java.util.Map;

public class LocalRegister {

    public static Map<String, Class> registeredServices = new HashMap<>();

    public static void register(String interfaceName, Class implClass) {
        registeredServices.put(interfaceName, implClass);
    }

    public static Class get(String interfaceName) {
        return registeredServices.get(interfaceName);
    }
}
