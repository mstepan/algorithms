package com.max.algs.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;

public final class NettyEchoClient {

    private static final Logger LOG = Logger.getLogger(NettyEchoClient.class);

    private NettyEchoClient() throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();

        try {

            Bootstrap boot = new Bootstrap();

            boot.group(group).
                    channel(NioSocketChannel.class).
                    remoteAddress(new InetSocketAddress("localhost", NettyEchoServer.PORT)).
                    handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture f = boot.connect().sync();
            f.channel().closeFuture().sync();
        }
        finally {
            group.shutdownGracefully().sync();
        }


        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new NettyEchoClient();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    @ChannelHandler.Sharable
    private static final class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(Unpooled.copiedBuffer("Hello from netty client", CharsetUtil.UTF_8));
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
            System.out.println(in.toString(CharsetUtil.UTF_8));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            //cause.printStackTrace();
            System.out.println("Who lets the dog out !!!");
            ctx.close();
        }
    }

}
