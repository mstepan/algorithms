package com.max.algs.creative;


import com.max.algs.string.StringUtils;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;


public final class BoyerMooreMatching {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * KBoyer-Moore substring matching algorithm.
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

        Map<Character, Integer> offset = calculateOffsets(pattern);

        final int patternLength = pattern.length();
        final int last = patternLength - 1;

        int i = last;
        int j = last;

        while (i < base.length()) {
            if (base.charAt(i) == pattern.charAt(j)) {
                --i;
                --j;

                if (j < 0) {
                    return i + 1;
                }
            }
            else {
                int index = offset.getOrDefault(base.charAt(j), -1);

                // not found
                if (index == -1) {
                    i += patternLength;
                    j = last;
                }
                else {
                    i += (index < j) ? (patternLength - index - 1) : patternLength;
                    j = last;
                }
            }
        }

        return -1;
    }

    private static Map<Character, Integer> calculateOffsets(String pattern) {

        assert pattern != null;

        Map<Character, Integer> offsets = new HashMap<>();

        for (int i = 0; i < pattern.length(); ++i) {
            offsets.put(pattern.charAt(i), i);
        }

        return offsets;
    }


    private BoyerMooreMatching() {

        for (int it = 0; it < 10000; ++it) {
            String base = StringUtils.generateDNAString(10000);
            String pattern = StringUtils.generateDNAString(9);

            int actualIndex = indexOf(base, pattern);
            int expectedIndex = base.indexOf(pattern);

            if (expectedIndex != actualIndex) {
                throw new AssertionError("expected = " + expectedIndex + ", actual = " + actualIndex);
            }

            LOG.info("passed: " + expectedIndex);
        }

        LOG.info("BoyerMooreMatching done...");
    }


    public static void main(String[] args) {
        try {
            new BoyerMooreMatching();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
