package com.kjr21362.common;

import java.io.Serializable;
import lombok.Data;

@Data
public abstract class Message implements Serializable {

    private int sequenceId;

    public static final int RPC_REQUEST_MESSAGE = 0;
    public static final int RPC_RESPONSE_MESSAGE = 1;

    public Message(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public abstract int getMessageType();

    public int getSequenceId() {
        return sequenceId;
    }
}
