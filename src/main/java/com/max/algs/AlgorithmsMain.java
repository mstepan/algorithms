package com.max.algs;


import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;


public final class AlgorithmsMain {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());


    private AlgorithmsMain() throws Exception {

        ackermanFunction(4, 8);

        LOG.info("AlgorithmsMain done...");
    }

    private static int ackermanFunction(int m, int n) {

        if (m == 0) {
            return n + 1;
        }

        if (n == 0) {
            return ackermanFunction(m - 1, 1);
        }

        return ackermanFunction(m - 1, ackermanFunction(m, n - 1));
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
