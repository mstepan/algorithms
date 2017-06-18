package com.max.algs.offheap;


import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;


public final class OffHeapMain {

    private static final Logger LOG = Logger.getLogger(OffHeapMain.class);

    private OffHeapMain() throws Exception {

        new OffHeapCleanerThread().start();

        System.out.println("OffHeapLongList creation started");

        final int capacity = 10000; //134_217_728; // number of longs that fit in 1GB RAM
        OffHeapLongList list = new OffHeapLongList(capacity);

        for (int i = 0; i < capacity; ++i) {
            list.add(i);
        }

        LOG.info("OffHeapLongList creation completed");

        list = null;

        TimeUnit.SECONDS.sleep(10);

        System.out.printf("OffHeapDsMain done: java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new OffHeapMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
