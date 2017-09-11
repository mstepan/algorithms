package com.max.algs;


import com.max.algs.util.MathUtils;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;


public final class AlgorithmsMain {


    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());


    private AlgorithmsMain() {
        double[] probabilities = {
                99.0 / 100.0,
                1.0 / 100.0
        };

        double entrophy = 0.0;

        for (double p : probabilities) {
            entrophy += p * MathUtils.log2(p);
        }

        entrophy = -entrophy;

        LOG.info("Entrophy: " + entrophy);

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
