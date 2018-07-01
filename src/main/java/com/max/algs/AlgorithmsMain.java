package com.max.algs;


import org.apache.log4j.Logger;

public final class AlgorithmsMain {

    private static final Logger LOG = Logger.getLogger(AlgorithmsMain.class);


    private AlgorithmsMain() throws Exception {

        Treap data = new Treap();

        for (int i = 0; i < 10; ++i) {
            data.add(i);
        }

        for (int i = 0; i < 20; ++i) {
            LOG.info("data.contains(" + i + "): " + data.contains(i));
        }

        LOG.info("Main completed.");

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
