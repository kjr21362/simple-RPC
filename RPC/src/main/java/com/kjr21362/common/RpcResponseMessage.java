package com.kjr21362.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RpcResponseMessage extends Message {

    private Object returnValue;
    private String err;

    public RpcResponseMessage(int sequenceId) {
        super(sequenceId);
    }

    @Override
    public int getMessageType() {
        return RPC_RESPONSE_MESSAGE;
    }
}
