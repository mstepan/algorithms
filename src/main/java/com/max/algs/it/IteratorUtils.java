package com.max.algs.it;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Iterator;

public final class IteratorUtils {

    private static final Logger LOG = Logger.getLogger(IteratorUtils.class);

    private IteratorUtils() {
        super();
        throw new IllegalStateException("Can't instantiate class '" + IteratorUtils.class.getName() + "'");
    }


    public static <T> void printAllArrayValues(Iterator<T[]> it) {

        if (it == null) {
            throw new IllegalArgumentException("Can't iterate over NULL iterator");
        }

        while (it.hasNext()) {
            LOG.info(Arrays.toString(it.next()));
        }

    }

    public static <T> void printAllValues(Iterator<T> it) {

        if (it == null) {
            throw new IllegalArgumentException("Can't iterate over NULL iterator");
        }

        while (it.hasNext()) {
            LOG.info(it.next());
        }

    }


}
