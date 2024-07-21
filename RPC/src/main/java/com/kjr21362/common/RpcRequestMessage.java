package com.kjr21362.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RpcRequestMessage extends Message {

    private String interfaceName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

    public RpcRequestMessage(int sequenceId, String interfaceName, String methodName, Class<?>[] parameterTypes,
                             Object[] parameters) {
        super(sequenceId);
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
    }

    @Override
    public int getMessageType() {
        return RPC_REQUEST_MESSAGE;
    }
}
