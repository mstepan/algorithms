package com.max.algs.offheap;


import java.util.concurrent.TimeUnit;


public final class OffHeapMain {


    private OffHeapMain() throws Exception {

        new OffHeapCleanerThread().start();

        System.out.println("OffHeapLongList creation started");

        final int capacity = 10000; //134_217_728; // number of longs that fit in 1GB RAM
        OffHeapLongList list = new OffHeapLongList(capacity);

        for (int i = 0; i < capacity; ++i) {
            list.add(i);
        }

        System.out.println("OffHeapLongList creation completed");

        list = null;
        System.gc();

        TimeUnit.SECONDS.sleep(10);

        System.out.printf("OffHeapDsMain done: java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new OffHeapMain();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
