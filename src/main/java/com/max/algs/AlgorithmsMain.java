package com.max.algs;


import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;


public final class AlgorithmsMain {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private AlgorithmsMain() {

//        MyPoint a = new MyPoint(0, 0, 1);
//        MyPoint b = new MyPoint(1, 1, 5);
//        MyPoint c = new MyPoint(2, 2, Integer.MIN_VALUE + 2);
//
//        LOG.info("a.compareTo(b): " + a.compareTo(b));
//        LOG.info("b.compareTo(c): " + b.compareTo(c));
//        LOG.info("a.compareTo(c): " + a.compareTo(c));

        LOG.info(Integer.MIN_VALUE);

        int res = 10 - Integer.MIN_VALUE;

        LOG.info(res);

        LOG.info("AlgorithmsMain done...");
    }


    public static void main(String[] args) {
        try {
            new AlgorithmsMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
