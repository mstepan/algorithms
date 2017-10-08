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
     * space: O(N*M) => can be reduced to O(min(N, M))
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

        final int rows = s1.length() + 1;
        final int cols = s2.length() + 1;

        boolean[][] sol = new boolean[rows][cols];
        sol[0][0] = true;


        // fill 1st row
        for (int col = 1; col < cols; ++col) {
            sol[0][col] = sol[0][col - 1] && (base.charAt(col - 1) == s2.charAt(col - 1));
        }

        for (int row = 1; row < rows; ++row) {

            sol[row][0] = sol[row - 1][0] && (base.charAt(row - 1) == s1.charAt(row - 1));

            boolean lastRowCombinedOrValue = sol[row][0];

            for (int col = 1; col < cols; ++col) {

                char baseCh = base.charAt(row + col - 1);
                char s1Ch = s1.charAt(row - 1);
                char s2Ch = s2.charAt(col - 1);

                sol[row][col] = (s1Ch == baseCh && sol[row - 1][col]) ||
                        (s2Ch == baseCh && sol[row][col - 1]);

                lastRowCombinedOrValue = lastRowCombinedOrValue || sol[row][col];
            }

            // last row was fully 'false',
            // just return 'false' and do not proceed further
            if (!lastRowCombinedOrValue) {
                return false;
            }
        }

        return sol[rows - 1][cols - 1];
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
