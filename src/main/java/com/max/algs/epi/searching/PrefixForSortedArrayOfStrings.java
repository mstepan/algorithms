package com.max.algs.epi.searching;


import org.apache.log4j.Logger;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 12.1. Variant.
 * Finds if 'p' is a prefix of a string in an array of sorted strings.
 */
public class PrefixForSortedArrayOfStrings {

    private static final Logger LOG = Logger.getLogger(PrefixForSortedArrayOfStrings.class);

    private PrefixForSortedArrayOfStrings() throws Exception {

        String[] arr = {"hell", "hello", "helping", "world"};
        Arrays.sort(arr);

        String prefix = "help";

        boolean hasPrefix = hasPrefix(arr, prefix);

        System.out.printf("hasPrefix: %s %n", hasPrefix);


        System.out.printf("'PrefixForSortedArrayOfStrings' completed. java-%s %n", System.getProperty("java.version"));
    }

    /**
     * time: O(lgN * K), N - arr.length, K - p.length
     * space: O(1)
     *
     * @throws NullPointerException if an arr contains null String value and the search algorithm hit this value.
     */
    private static boolean hasPrefix(String[] arr, String prefix) {
        checkNotNull(arr);
        checkNotNull(prefix);

        int lo = 0;
        int hi = arr.length - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            String cur = arr[mid];

            int cmpRes = prefixCompare(cur, prefix);

            if (cmpRes == 0) {
                return true;
            }

            if (cmpRes < 0) {
                lo = mid + 1;

            }
            else {
                hi = mid - 1;
            }
        }

        return false;
    }

    private static int prefixCompare(String str, String prefix) {
        assert str != null : "null 'str' value";
        assert prefix != null : "null 'prefix'";

        for (int i = 0; i < Math.min(str.length(), prefix.length()); ++i) {
            int cmp = Character.compare(str.charAt(i), prefix.charAt(i));

            if (cmp != 0) {
                return cmp;
            }
        }

        return prefix.length() <= str.length() ? 0 : -1;
    }

    public static void main(String[] args) {
        try {
            new PrefixForSortedArrayOfStrings();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
