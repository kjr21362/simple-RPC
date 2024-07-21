package com.kjr21362.common;

import java.util.concurrent.atomic.AtomicInteger;

public class SequenceIdGenerator {

    private static final AtomicInteger sequenceNum = new AtomicInteger(0);

    public static int getNext() {
        return sequenceNum.incrementAndGet();
    }
}
