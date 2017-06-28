package com.max.algs.epi.hashing;


import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 13.6. Find the nearest repeated entries in an array.
 */
public final class NearestRepeatedEntries {

    private static final Logger LOG = Logger.getLogger(NearestRepeatedEntries.class);

    private NearestRepeatedEntries() throws Exception {

        String[] arr = "All work and no play makes for no work no fun and o results".split(" ");

        int minDistance = findMinRepeatedDistance(arr);

        System.out.printf("minDistance = %d %n", minDistance);

        System.out.printf("NearestRepeatedEntries: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * N - all words from input.
     * M - distinct words.
     * <p>
     * time: O(N)
     * space: O(M)
     */
    public static int findMinRepeatedDistance(String[] arr) {
        checkNotNull(arr);

        Map<String, Integer> lastWordPos = new HashMap<>();

        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < arr.length; ++i) {
            String word = arr[i];
            checkNotNull(word);

            word = word.toLowerCase();

            Integer prevPos = lastWordPos.put(word, i);

            if (prevPos != null) {
                minDistance = Math.min(minDistance, i - prevPos);
            }
        }

        return minDistance == Integer.MAX_VALUE ? -1 : minDistance;
    }

    public static void main(String[] args) {
        try {
            new NearestRepeatedEntries();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}

