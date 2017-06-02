package com.max.algs.epi.hashing;


import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 13.6. Find the nearest repeated entries in an array.
 */
public final class NearestRepeatedEntries {

    /**
     *
     * N - all words from input.
     * M - distinct words.
     *
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


    private NearestRepeatedEntries() throws Exception {

        String[] arr = "hello world World".split(" "); //, "All work and no play makes for no work no fun and o results".split(" ");

        int minDistance = findMinRepeatedDistance(arr);

        System.out.printf("minDistance = %d %n", minDistance);

        System.out.printf("NearestRepeatedEntries: java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new NearestRepeatedEntries();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
