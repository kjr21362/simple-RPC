package com.kjr21362.handler;

import com.kjr21362.common.RpcResponseMessage;
import com.kjr21362.protocol.NettyClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyResponseHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponseMessage rpcResponseMessage) {
        log.debug("{}", rpcResponseMessage);
        Promise<Object> promise = NettyClient.PROMISE_MAP.remove(rpcResponseMessage.getSequenceId());

        if (promise != null) {
            Object returnValue = rpcResponseMessage.getReturnValue();
            String err = rpcResponseMessage.getErr();
            if (err != null) {
                promise.setFailure(new RuntimeException(err));
            } else {
                promise.setSuccess(returnValue);
            }
        }
    }
}
