package com.max.algs.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public final class NettyEchoServer {

    static final int PORT = 7777;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");


    @ChannelHandler.Sharable
    private static final class EchoHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf in = (ByteBuf) msg;
            String msgStr = in.toString(CharsetUtil.UTF_8);
            System.out.printf("Received from client: %s", msgStr);
            ctx.writeAndFlush(Unpooled.copiedBuffer("Response: " + msgStr, Charset.defaultCharset()));
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }

    private NettyEchoServer() throws Exception {

        EchoHandler echoHandler = new EchoHandler();
        EventLoopGroup mainGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap boot = new ServerBootstrap();
            boot.group(mainGroup).
                    channel(NioServerSocketChannel.class).
                    localAddress(new InetSocketAddress(PORT)).
                    childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // we can use the same instance of 'echoHandler' for every connection
                            // because EchoHandler is stateless and @Sharable
                            ch.pipeline().addLast(echoHandler);
                        }
                    });

            ChannelFuture f = boot.bind().sync();

            System.out.println("Server started at port " + PORT);

            f.channel().closeFuture().sync();
        }
        finally {
            mainGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) {
        try {
            new NettyEchoServer();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

