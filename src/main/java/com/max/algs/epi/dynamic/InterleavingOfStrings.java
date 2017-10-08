package com.max.algs.epi.dynamic;


import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;

import static com.google.common.base.Preconditions.checkNotNull;


public final class InterleavingOfStrings {


    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());


    /**
     * Check is 'base' string is interleaving of 's1' and 's2' strings.
     * <p>
     * N - s1.length()
     * M - s2.length()
     * <p>
     * time: O(N*M)
     * space: O(min(N, M))
     */
    private static boolean isInterleaving(String base, String s1, String s2) {
        checkNotNull(base);
        checkNotNull(s1);
        checkNotNull(s2);

        if (base.length() != s1.length() + s2.length()) {
            return false;
        }

        if (s1.length() == 0) {
            return base.equals(s2);
        }

        if (s2.length() == 0) {
            return base.equals(s1);
        }

        String minStr = s1;
        String otherStr = s2;

        if (s2.length() < s1.length()) {
            minStr = s2;
            otherStr = s1;
        }

        final int rows = otherStr.length() + 1;
        final int cols = minStr.length() + 1;

        boolean[] prev = new boolean[cols];
        prev[0] = true;

        // fill 1st row
        for (int col = 1; col < cols; ++col) {
            prev[col] = prev[col - 1] && (base.charAt(col - 1) == minStr.charAt(col - 1));
        }

        for (int row = 1; row < rows; ++row) {

            boolean[] cur = new boolean[cols];
            cur[0] = prev[0] && (base.charAt(row - 1) == otherStr.charAt(row - 1));

            boolean lastRowCombinedOrValue = cur[0];

            for (int col = 1; col < cols; ++col) {

                char baseCh = base.charAt(row + col - 1);
                char otherCh = otherStr.charAt(row - 1);
                char minCh = minStr.charAt(col - 1);

                cur[col] = (otherCh == baseCh && prev[col]) ||
                        (minCh == baseCh && cur[col - 1]);

                lastRowCombinedOrValue = lastRowCombinedOrValue || cur[col];
            }

            // last row was fully 'false',
            // just return 'false' and do not proceed further
            if (!lastRowCombinedOrValue) {
                return false;
            }

            prev = cur;
        }

        return prev[cols - 1];
    }

    private InterleavingOfStrings() {

        String base = "hewlorllod";
//        String base = "hewlrloold";
        String s1 = "hello";
        String s2 = "world";

        LOG.info("isInterleaving: " + isInterleaving(base, s1, s2));

        LOG.info("InterleavingOfStrings done...");
    }


    public static void main(String[] args) {
        try {
            new InterleavingOfStrings();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
