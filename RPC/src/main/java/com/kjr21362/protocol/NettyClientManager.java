package com.kjr21362.protocol;

import com.kjr21362.codec.MessageCodec;
import com.kjr21362.codec.RpcProtocolFrameDecoder;
import com.kjr21362.handler.NettyResponseHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClientManager {

    private final String host;
    private final int port;

    private static Channel channel = null;
    private static final Object LOCK = new Object();

    public NettyClientManager(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Channel getChannel() {
        if (channel != null) {
            return channel;
        }

        synchronized (LOCK) {
            if (channel != null) {
                return channel;
            }

            init();
            return channel;
        }
    }

    private void init() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    socketChannel.pipeline().addLast(
                        new RpcProtocolFrameDecoder(),
                        new LoggingHandler(LogLevel.INFO),
                        new MessageCodec(),
                        new NettyResponseHandler());
                }
            });

            channel = b.connect(host, port).sync().channel();
            channel.closeFuture().addListener(_ -> workerGroup.shutdownGracefully());

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
