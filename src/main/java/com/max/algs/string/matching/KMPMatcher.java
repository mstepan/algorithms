package com.max.algs.string.matching;


import java.util.Arrays;

public class KMPMatcher {

    /**
     * Build prefix table for KMP algorithm.
     * time: O(M)
     * space: O(M)
     * @param pattern - pattern string with 'M' length
     * @return prefix table
     */
    private static int[] buildPrefix(String pattern) {

        int[] prefix = new int[pattern.length()];

        for (int i = 1; i < pattern.length(); i++) {
            int prevMatch = prefix[i - 1];

            if (pattern.charAt(i) == pattern.charAt(prevMatch)) {
                prefix[i] = prevMatch + 1;
            }
            else {
                prefix[i] = (pattern.charAt(0) == pattern.charAt(i) ? 1 : 0);
            }
        }

        return prefix;
    }


    public static void main(String[] args) {

        String text = "";
        String pattern = "abcabcdabcabca";

        int[] prefix = buildPrefix(pattern);

        System.out.println(Arrays.toString(prefix));

        System.out.printf("Main done...");
    }
}
