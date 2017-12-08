package com.max.algs.creative;

import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;

import static com.google.common.base.Preconditions.checkNotNull;

final class LongestStutteringSubSequence {


    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * time: O( (N+M)*lg(N/M) )
     * space: O(1)
     */
    static int longestStutteringSubSequence(String base, String str) {
        checkNotNull(base);
        checkNotNull(str);

        if (base.length() < str.length()) {
            return 0;
        }

        if (base.equals(str)) {
            return 1;
        }

        final int baseLength = base.length();
        final int strLength = str.length();

        int longestLength = 0;
        int lo = 1;
        int hi = baseLength / strLength;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            if (isStutteringSubSequence(base, str, mid)) {
                longestLength = mid;
                lo = mid + 1;
            }
            else {
                hi = mid - 1;
            }
        }

        return longestLength;
    }

    private static boolean isStutteringSubSequence(String base, String str, int count) {

        int strIndex = 0;
        int left = count;

        for (int i = 0; i < base.length() && strIndex < str.length(); ++i) {

            if (base.charAt(i) == str.charAt(strIndex)) {
                --left;

                if (left == 0) {
                    left = count;
                    ++strIndex;
                }
            }
        }

        return strIndex == str.length();
    }

    private LongestStutteringSubSequence() {

        String base = "KTXLXYPZYZXZZKZXYZTZXYXXYZZKZTYUZOZUZTYXYXRTX";
        String str = "XYZZX";

        int longest = longestStutteringSubSequence(base, str);

        LOG.info("longest sub sequence: " + longest);

        LOG.info("LongestStutteringSubSequence done...");
    }


    public static void main(String[] args) {
        try {
            new LongestStutteringSubSequence();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
