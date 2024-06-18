package com.kjr21362.common;

import java.io.Serializable;
import java.util.Arrays;

public class InvocationParams implements Serializable {

    private String interfaceName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

    public InvocationParams(String interfaceName, String methodName, Class<?>[] parameterTypes,
                            Object[] parameters) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return STR."InvocationParams {interfaceName = \{interfaceName}, methodName = \{methodName}, parameterTypes = \{Arrays.toString(
            parameterTypes)}, parameters = \{Arrays.toString(parameters)} }";
    }
}
