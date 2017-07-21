package com.max.algs;


import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


public final class AlgorithmsMain {


    private static final Logger LOG = Logger.getLogger(AlgorithmsMain.class);


    public static int longestIncreasedSubsequence(int[] arr) {
        checkNotNull(arr);

        int[] sol = new int[arr.length];

        for (int i = 0; i < arr.length; ++i) {

            int maxWithCur = 0;
            int val = arr[i];

            for (int j = i - 1; j >= 0; --j) {
                if (arr[j] < val) {
                    maxWithCur = Math.max(maxWithCur, sol[j]);
                }
            }

            sol[i] = 1 + maxWithCur;
        }

        List<Integer> incSubSeq = reconstructSolution(sol, arr);

        LOG.info("incSubSeq: " + incSubSeq);

        return sol[sol.length - 1];
    }

    private static List<Integer> reconstructSolution(int[] sol, int[] arr) {

        int maxIndex = 0;

        for (int i = 1; i < sol.length; ++i) {
            if (sol[i] > sol[maxIndex]) {
                maxIndex = i;
            }
        }

        List<Integer> seq = new ArrayList<>();
        int index = maxIndex;

        while (sol[index] != 1) {

            assert index >= 0 : "Negative 'index' detected";

            seq.add(arr[index]);

            for (int k = index - 1; k >= 0; --k) {

                assert k >= 0 : "Negative 'k' detected";

                if (arr[k] < arr[index] && sol[k] == sol[index] - 1) {
                    index = k;
                    break;
                }
            }
        }

        seq.add(arr[index]);

        Collections.reverse(seq);

        return seq;
    }

    private AlgorithmsMain() {

        int[] arr = {3, 5, -6, 4, 10, 3, 1, 13};

        int length = longestIncreasedSubsequence(arr);

        LOG.info("longestIncreasedSubsequence: " + length);

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