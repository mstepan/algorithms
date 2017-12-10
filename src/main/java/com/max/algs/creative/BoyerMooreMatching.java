package com.max.algs.creative;


import com.max.algs.string.StringUtils;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
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

        final Map<Character, Integer> offset = calculateOffsets(pattern);

        final int patternLength = pattern.length();
        final int last = patternLength - 1;

        int i = last;
        int j = last;

        while (i < base.length()) {

            if (base.charAt(i) == pattern.charAt(j)) {

                if (j == 0) {
                    return i;
                }

                --i;
                --j;
            }
            else {
                int index = offset.getOrDefault(base.charAt(i), -1);

                // base[i] not found, shift a whole pattern
                if (index == -1) {
                    i += patternLength;
                }
                else {
                    i += (index < j) ? (last - index) : (last - j + 1);
                }

                j = last;
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

        return Collections.unmodifiableMap(offsets);
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
        }

//
//        String base = "xyxxyxxyyxyxyxyyxyxyxx";
//        String pattern = "xyxyyxyxyxx";
//
//        int index = indexOf(base, pattern);
//        LOG.info("expected = " + base.indexOf(pattern) + ", actual = " + index);

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
