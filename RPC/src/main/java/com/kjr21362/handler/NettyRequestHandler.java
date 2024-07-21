package com.kjr21362.handler;

import com.kjr21362.common.RpcRequestMessage;
import com.kjr21362.common.RpcResponseMessage;
import com.kjr21362.register.LocalRegister;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyRequestHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequestMessage rpcRequestMessage) {
        RpcResponseMessage rpcResponseMessage = new RpcResponseMessage(rpcRequestMessage.getSequenceId());
        try {
            String interfaceName = rpcRequestMessage.getInterfaceName();

            Class<?> implClass = LocalRegister.get(interfaceName);
            Method method = implClass.getMethod(rpcRequestMessage.getMethodName(),
                rpcRequestMessage.getParameterTypes());

            Object result = method.invoke(implClass.getDeclaredConstructor().newInstance(),
                rpcRequestMessage.getParameters());

            rpcResponseMessage.setReturnValue(result);
        } catch (Exception e) {
            log.debug(e.toString());
            rpcResponseMessage.setErr("Server Error: " + e.getCause().getMessage());
        }

        channelHandlerContext.writeAndFlush(rpcResponseMessage);
    }
}
