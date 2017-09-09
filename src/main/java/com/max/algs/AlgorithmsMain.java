package com.max.algs;


import com.max.algs.util.MathUtils;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;


public final class AlgorithmsMain {


    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());


    private AlgorithmsMain() {

        int bitsCount = (int) Math.ceil(MathUtils.log2(6.0));
        int charsCount = 100_000;
        LOG.info("total bits: " + (bitsCount * charsCount));

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
