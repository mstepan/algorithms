package com.max.algs.epi.hashing;


import org.apache.log4j.Logger;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.max.algs.epi.hashing.SmallestSubarrayCoveringAllValues1.Subarray;

/**
 * 13.7. Find the smallest subarray covering all values.
 */
public final class SmallestSubarrayCoveringAllValues2 {

    private static final Logger LOG = Logger.getLogger(SmallestSubarrayCoveringAllValues2.class);

    private SmallestSubarrayCoveringAllValues2() throws Exception {

        String[] textToSearch = "hello the wonderful and beautiful world this world is hello like".split(" ");

        Set<String> searchKeywords = new HashSet<>(Arrays.asList("hello", "world"));

        Subarray res = findShortestDigest(textToSearch, searchKeywords);

        System.out.println(res);

        System.out.printf("SmallestSubarrayCoveringAllValues: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * N - arr.length
     * M - search.size()
     * <p>
     * time: O(N)
     * space: O(M)
     */
    public static Subarray findShortestDigest(String[] arr, Set<String> search) {
        checkNotNull(arr);
        checkNotNull(search);

        PositionMap map = new PositionMap();

        return Subarray.EMPTY;
    }

    public static void main(String[] args) {
        try {
            new SmallestSubarrayCoveringAllValues2();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private static final class PositionMap {
        final Map<String, Integer> data = new LinkedHashMap<>();

        int minVal = Integer.MAX_VALUE;
        int maxVal = Integer.MIN_VALUE;

        int size() {
            return data.size();
        }

        void put(String key, int val) {

            if (data.containsKey(key)) {
                int prevPos = data.remove(key);

                // find new minimum
                if (prevPos == minVal) {
                    minVal = data.values().iterator().next();
                }

                data.put(key, val);

            }

        }
    }
}

