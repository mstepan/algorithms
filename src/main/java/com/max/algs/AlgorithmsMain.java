package com.max.algs;


import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;


public final class AlgorithmsMain {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private AlgorithmsMain() {

        String base = "hello wonderful world";
        String pattern = "wonder";

        int index = base.indexOf(pattern);

        base.contains(pattern);

        LOG.info("index = " + index);

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
