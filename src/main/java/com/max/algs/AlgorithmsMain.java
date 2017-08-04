package com.max.algs;


import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;


public final class AlgorithmsMain {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());


    public static void main(String[] args) {
        try {
            new AlgorithmsMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
