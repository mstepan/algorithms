package com.max.algs.epi.hashing;


import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 13.10. Find the length of a longest contained interval.
 */
public final class LongestContainedInterval {

    private static final Logger LOG = Logger.getLogger(LongestContainedInterval.class);

    private LongestContainedInterval() throws Exception {

        int[] arr = {3, -2, 7, 9, 8, 1, 2, 0, -1, 5, 8};

        int length = findLongestContainedIntervalLength(arr);

        System.out.printf("longest interval length = %d %n", length);

        System.out.printf("LongestContainedInterval: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * N - arr.length
     * D - distinct elements count in 'arr'
     * <p>
     * time: O(N)
     * space: O(D)
     */
    public static int findLongestContainedIntervalLength(int[] arr) {
        checkNotNull(arr);

        if (arr.length < 2) {
            return arr.length;
        }

        Map<Integer, Integer> freq = new HashMap<>();

        for (int val : arr) {
            freq.compute(val, (key, cnt) -> cnt == null ? 1 : cnt + 1);
        }

        int maxLength = 1;

        for (int i = 0; i < arr.length && !freq.isEmpty(); ++i) {

            if (freq.containsKey(arr[i])) {

                int leftValue = findLeftmostValue(arr[i], freq);

                assert leftValue <= arr[i] : "leftValue > arr[i]";
                assert freq.containsKey(leftValue) : "leftValue not exists in freq map";

                int curLength = 0;

                while (freq.containsKey(leftValue)) {
                    curLength += freq.get(leftValue);

                    freq.remove(leftValue);
                    ++leftValue;
                }

                assert curLength > 0 : "curLength == 0";

                maxLength = Math.max(maxLength, curLength);
            }
        }

        assert freq.isEmpty() : "freq is not empty";

        return maxLength;
    }

    private static int findLeftmostValue(int baseValue, Map<Integer, Integer> map) {

        int val = baseValue;

        while (map.containsKey(val)) {
            --val;
        }

        return val + 1;
    }

    public static void main(String[] args) {
        try {
            new LongestContainedInterval();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}

