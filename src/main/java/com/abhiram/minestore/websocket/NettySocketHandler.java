package com.abhiram.minestore.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettySocketHandler extends ChannelInitializer<SocketChannel> {


    /**
     * this methods inits minestore command handler...
     * @param serverSocket
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel serverSocket) throws Exception {
        serverSocket.pipeline().addLast(new StringEncoder());
        serverSocket.pipeline().addLast(new StringDecoder());
        serverSocket.pipeline().addLast(new MinestoreCommandHandler());
    }
}
