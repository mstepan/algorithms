package com.max.algs.ds.dht.cache_node;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;

import java.net.InetSocketAddress;


public final class CacheNode implements Runnable {


    private static final Logger LOG = Logger.getLogger(CacheNode.class);

    private final String name;
    private final int port;

    public CacheNode(String name, int port) {
        this.name = name;
        this.port = port;
    }


    @Override
    public void run() {
        try {
            Server server = new Server(new InetSocketAddress(port));
            ContextHandler context = new ContextHandler();
            context.setContextPath("/cache");
            context.setResourceBase(".");
            context.setClassLoader(Thread.currentThread().getContextClassLoader());
            server.setHandler(context);

            context.setHandler(new CacheHandler(name));
            server.start();

            LOG.info("Listening on port: " + port);

            server.join();
        }
        catch (Exception ex) {
            LOG.error("Can't start embedded jetty server", ex);
        }

    }


}
