package com.max.algs.epi.searching;


import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class MissedAndDuplicateMain {

    private static final Logger LOG = Logger.getLogger(MissedAndDuplicateMain.class);

    private MissedAndDuplicateMain() throws Exception {

        int[] arr = new int[]{5, 4, 3, 4, 1, 0};

        MissedAndDuplicate res = findMissedAndDuplicate(arr);

        System.out.println(res);

        System.out.printf("MissedAndDuplicateMain done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * Given array with elements from [0; arr.length-1] range.
     * <p>
     * One element repeated 2 times and one elements is missed. Find missed element and duplicate.
     * <p>
     * time: O(N)
     * space: O(1)
     */
    public static MissedAndDuplicate findMissedAndDuplicate(int[] arr) {
        checkNotNull(arr);

        int missedAndDup = 0;

        for (int i = 0; i < arr.length; ++i) {

            checkArgument(arr[i] >= 0 && arr[i] < arr.length,
                    "Array element '%s' is out of range: [%s;%s]", arr[i], 0, arr.length - 1);

            missedAndDup ^= i;
            missedAndDup ^= arr[i];
        }

        int mask = missedAndDup & (~(missedAndDup - 1));

        int elem1 = 0;

        for (int i = 0; i < arr.length; ++i) {

            if ((i & mask) != 0) {
                elem1 ^= i;
            }

            if ((arr[i] & mask) != 0) {
                elem1 ^= arr[i];
            }
        }

        int elem2 = missedAndDup ^ elem1;

        for (int val : arr) {
            if (val == elem1) {
                return new MissedAndDuplicate(elem2, elem1);
            }
        }

        return new MissedAndDuplicate(elem1, elem2);
    }

    public static void main(String[] args) {
        try {
            new MissedAndDuplicateMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private static final class MissedAndDuplicate {
        final int missed;
        final int duplicate;

        MissedAndDuplicate(int missed, int duplicate) {
            this.missed = missed;
            this.duplicate = duplicate;
        }

        @Override
        public String toString() {
            return "missed: " + missed + ", duplicate: " + duplicate;
        }
    }
}

