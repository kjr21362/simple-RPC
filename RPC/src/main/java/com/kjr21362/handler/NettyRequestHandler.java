package com.kjr21362.handler;

import com.kjr21362.common.RpcRequestMessage;
import com.kjr21362.common.RpcResponseMessage;
import com.kjr21362.provider.ZookeeperServiceProvider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyRequestHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {

    private final ZookeeperServiceProvider zookeeperServiceProvider;

    public NettyRequestHandler(){
        zookeeperServiceProvider = ZookeeperServiceProvider.getInstance();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequestMessage rpcRequestMessage) {
        RpcResponseMessage rpcResponseMessage = new RpcResponseMessage(rpcRequestMessage.getSequenceId());
        try {
            String interfaceName = rpcRequestMessage.getInterfaceName();
            Object implClass = zookeeperServiceProvider.getService(interfaceName);
            Method method = implClass.getClass().getMethod(rpcRequestMessage.getMethodName(), rpcRequestMessage.getParameterTypes());
            Object result = method.invoke(implClass, rpcRequestMessage.getParameters());

            rpcResponseMessage.setReturnValue(result);
        } catch (Exception e) {
            log.error(e.toString());
            Throwable throwable = e.getCause();
            String err = "Server Error.";
            if(throwable != null){
                err += throwable.getMessage();
            }
            rpcResponseMessage.setErr(err);
        }

        channelHandlerContext.writeAndFlush(rpcResponseMessage);
    }
}
