package com.max.algs.creative;


import com.max.algs.string.StringUtils;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;

import static com.google.common.base.Preconditions.checkNotNull;


public final class KMPMatching {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Knuth-Morris-Pratt substring matching algorithm.
     * time: O(N + M)
     * space: O(M)
     * <p>
     * N - base.length()
     * M - pattern.length()
     */
    private static int indexOf(String base, String pattern) {
        checkNotNull(base);
        checkNotNull(pattern);

        if (base.length() < pattern.length()) {
            return -1;
        }

        if (base.length() == pattern.length()) {
            return base.equals(pattern) ? 0 : -1;
        }

        int[] prefix = calculatePrefixFunction(pattern);

        assert prefix != null;
        assert prefix.length == pattern.length();

        int i = 0;
        int j = 0;

        while (i < base.length() && j < pattern.length()) {

            assert i >= 0 && i < base.length();
            assert j >= 0 && j <= pattern.length();

            // chars matched
            if (base.charAt(i) == pattern.charAt(j)) {
                ++i;
                ++j;
            }
            // chars mismatched
            else {
                if (j == 0) {
                    ++i;
                }
                else {
                    j = prefix[j - 1];
                }
            }
        }

        // match found
        if (j == pattern.length()) {
            return i - pattern.length();
        }

        return -1;
    }

    /**
     * time: O(N)
     * space: O(N)
     */
    private static int[] calculatePrefixFunction(String str) {

        assert str != null;
        assert str.length() > 0;

        int[] prefix = new int[str.length()];

        for (int i = 1; i < str.length(); ++i) {
            int prefixIndex = i - 1;

            assert prefixIndex >= 0;

            char ch = str.charAt(i);

            while (prefixIndex >= 0) {
                int index = prefix[prefixIndex];

                if (ch == str.charAt(index)) {
                    prefix[i] = prefix[prefixIndex] + 1;
                    break;
                }

                prefixIndex = index - 1;
            }
        }

        return prefix;
    }

    private KMPMatching() {

        for (int it = 0; it < 10000; ++it) {
            String base = StringUtils.generateDNAString(10000);
            String pattern = StringUtils.generateDNAString(9);

            int actualIndex = indexOf(base, pattern);
            int expectedIndex = base.indexOf(pattern);

            if (expectedIndex != actualIndex) {
                throw new AssertionError("expected = " + expectedIndex + ", actual = " + actualIndex);
            }
        }

        LOG.info("KMPMatching done...");
    }


    public static void main(String[] args) {
        try {
            new KMPMatching();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
