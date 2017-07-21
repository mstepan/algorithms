package com.max.algs.dynamic;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Given an array of integers write code that returns length of the longest monotonically
 * increased sub sequence in the array.
 */
public class LongestIncreasedSubsequence {

    private static final Logger LOG = Logger.getLogger(LongestIncreasedSubsequence.class);

    /**
     * Dynamic programming solution.
     * <p>
     * time: O(N)
     * space: O(N)
     */
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

    /**
     * time: O(N)
     * space: O(seqLength)
     */
    private static List<Integer> reconstructSolution(int[] sol, int[] arr) {

        int maxIndex = 0;

        for (int i = 1; i < sol.length; ++i) {
            if (sol[i] > sol[maxIndex]) {
                maxIndex = i;
            }
        }

        List<Integer> seq = new ArrayList<>();
        int index = maxIndex;

        while (index >= 0) {

            seq.add(arr[index]);

            int k = index - 1;

            while (k >= 0) {
                if (arr[k] < arr[index] && sol[k] == sol[index] - 1) {
                    break;
                }

                --k;
            }

            index = k;
        }

        Collections.reverse(seq);

        return seq;
    }

    private LongestIncreasedSubsequence() {

        int[] arr = {3, 5, -6, 4, 10, 3, 1, 13};

        int length = longestIncreasedSubsequence(arr);

        LOG.info("longestIncreasedSubsequence: " + length);

        LOG.info("LongestIncreasedSubsequence done: java-" + System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new LongestIncreasedSubsequence();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
