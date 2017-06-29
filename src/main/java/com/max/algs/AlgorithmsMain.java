package com.max.algs;


import com.max.algs.it.BaseAllValuesIterator;
import org.apache.log4j.Logger;

import java.util.Iterator;

public final class AlgorithmsMain {

    private static final Logger LOG = Logger.getLogger(AlgorithmsMain.class);


    private AlgorithmsMain() {

        int base = 3;
        int length = 5;

        Iterator<String> it = new BaseAllValuesIterator(base, length);

        while (it.hasNext()) {
            LOG.info(it.next());
        }

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

