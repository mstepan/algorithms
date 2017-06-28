package com.max.algs.epi.string.matching;

import com.max.algs.string.StringUtils;
import org.apache.log4j.Logger;

import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;


public class KMPStringMatching {

    private static final Logger LOG = Logger.getLogger(KMPStringMatching.class);

    private KMPStringMatching() throws Exception {

        Random rand = new Random();

        for (int i = 0; i < 10_000; ++i) {
            String str = StringUtils.generateDNAString(20 + rand.nextInt(5000));
            String pattern = StringUtils.generateDNAString(3 + rand.nextInt(10));

            int actualIndex = find(str, pattern, true);
            int expectedIndex = str.indexOf(pattern);

//            System.out.printf("actualIndex = %d, expectedIndex = %d %n", actualIndex, expectedIndex);

            if (actualIndex != expectedIndex) {
                throw new IllegalStateException("actualIndex != expectedIndex");
            }
        }

//        int[] lps = longestPrefixSuffix("abxabqabxabrabxabqabxabx");
//        System.out.printf("lps:    %s %n", Arrays.toString(lps));
//
//        int[] lpsOpt = longestPrefixSuffixOptimized("abxabqabxabrabxabqabxabx");
//        System.out.printf("lpsOpt: %s %n", Arrays.toString(lpsOpt));
//
//        int[] lpsOpt2 = longestPrefixSuffixOptimized("abcxabcde");
//        System.out.printf("lpsOpt2: %s %n", Arrays.toString(lpsOpt2));

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * Knuth-Morris-Pratt exact string mathcing algorithm.
     * <p>
     * Find first occurrence of 'pattern' in 'str'.
     * <p>
     * N - str.length()
     * M - pattern.length()
     * <p>
     * time: O(N + M)
     * space: O(M)
     */
    public static int find(String str, String pattern, boolean optimized) {
        checkArgument(pattern != null, "null 'pattern' passed");
        checkArgument(str != null, "null 'str' passed");
        checkArgument(str.length() >= pattern.length());

        if (pattern == str) {
            return 0;
        }

        final int strLength = str.length();
        final int patternLength = pattern.length();

        if (patternLength > strLength) {
            return -1;
        }

        int j = 0;
        int i = 0;

        int[] lps = optimized ? longestPrefixSuffixOptimized(pattern) : longestPrefixSuffix(pattern);

        while (i < strLength) {
            // chars are matched, increment 'i' and 'j'
            if (pattern.charAt(j) == str.charAt(i)) {
                ++i;
                ++j;
            }
            // mismatch at position 'j' of 'pattern' and position 'i' of 'str'
            else {
                // mismatch at '0' position, increment 'i'
                if (j == 0) {
                    ++i;
                }
                // shift 'pattern' lps[j - 1] characters
                else {
                    j = lps[j - 1];
                }
            }

            // pattern fully matched
            if (j == pattern.length()) {
                return i - j;
            }
        }

        // match at the end of 'str'
        if (j == pattern.length()) {
            return str.length() - pattern.length();
        }

        return -1;
    }

    /**
     * Calculate longest prefix that is also the suffix of the string for each position.
     * <p>
     * time: O(N)
     * space: O(N)
     */
    private static int[] longestPrefixSuffix(String str) {
        assert str != null : "null 'str' parameter passed";

        int[] lps = new int[str.length()];
        char ch;
        int prev;

        for (int i = 1, lpsLength = lps.length; i < lpsLength; ++i) {

            ch = str.charAt(i);
            prev = lps[i - 1];

            while (prev != 0 && str.charAt(prev) != ch) {
                prev = lps[prev - 1];
            }

            if (str.charAt(prev) == ch) {
                lps[i] = prev + 1;
            }
            // else lps[i] = 0;
        }

        return lps;
    }

    /**
     * Define 'LPSopt' to be the length of the longest proper suffix of P[0..i]
     * that matches a prefix of P, with the added condition that characters P[i + 1] and P[LPSopt] are unequal.
     * <p>
     * LPSopt[i] <= LPS[i] for all 'i'.
     * <p>
     * time: O(N)
     * space: O(N)
     */
    private static int[] longestPrefixSuffixOptimized(String str) {
        int[] lps = longestPrefixSuffix(str);

        for (int i = 1; i < lps.length - 1; ++i) {

            int prev = lps[i];

            if (str.charAt(prev) == str.charAt(i + 1)) {
                lps[i] = (prev == 0 ? 0 : lps[prev - 1]);
            }
        }

        return lps;
    }

    public static void main(String[] args) {
        try {
            new KMPStringMatching();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
