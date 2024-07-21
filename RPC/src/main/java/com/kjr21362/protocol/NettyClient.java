package com.kjr21362.protocol;

import com.kjr21362.common.InvocationParams;
import com.kjr21362.common.RpcRequestMessage;
import com.kjr21362.common.SequenceIdGenerator;
import io.netty.channel.Channel;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClient {

    private final NettyClientManager nettyClientManager;

    public static final Map<Integer, Promise<Object>> PROMISE_MAP = new ConcurrentHashMap<>();

    public NettyClient(String host, int port) {
        nettyClientManager = new NettyClientManager(host, port);
    }

    public Object run(InvocationParams invocationParams) {

        try {
            Channel channel = nettyClientManager.getChannel();

            int sequenceId = SequenceIdGenerator.getNext();
            RpcRequestMessage rpcRequestMessage =
                new RpcRequestMessage(sequenceId, invocationParams.getInterfaceName(), invocationParams.getMethodName(),
                    invocationParams.getParameterTypes(), invocationParams.getParameters());
            channel.writeAndFlush(rpcRequestMessage);

            DefaultPromise<Object> promise = new DefaultPromise<>(channel.eventLoop());
            PROMISE_MAP.put(sequenceId, promise);
            promise.await();
            if (promise.isSuccess()) {
                log.debug("Promise success");
                return promise.getNow();
            } else {
                log.debug("Promise failure");
                throw new RuntimeException(promise.cause());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
