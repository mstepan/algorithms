package com.max.algs;


import org.apache.log4j.Logger;


public final class AlgorithmsMain {


    private static final Logger LOG = Logger.getLogger(AlgorithmsMain.class);

    private AlgorithmsMain() {

        LOG.info("Main done: java-" + System.getProperty("java.version"));
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