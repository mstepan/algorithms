package com.max.algs;


import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;


public final class AlgorithmsMain {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());


    private AlgorithmsMain() {


        LOG.info("AlgorithmsMain done...");
    }

    private static void reverse(int[] arr) {
        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;

            ++left;
            --right;
        }
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
