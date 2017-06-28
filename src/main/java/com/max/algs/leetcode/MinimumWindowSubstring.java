package com.max.algs.leetcode;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Given a string S and a string T, find the minimum window in S which will contain all
 * the characters in T in complexity O(n).
 * <p>
 * For example,
 * S = "ADOBECODEBANC"
 * T = "ABC"
 * Minimum window is "BANC".
 * <p>
 * Note:
 * If there is no such window in S that covers all characters in T, return the empty string "".
 * <p>
 * If there are multiple such windows, you are guaranteed that there will always be only one unique minimum window in S.
 * <p>
 * See: https://leetcode.com/problems/minimum-window-substring/
 */
public class MinimumWindowSubstring {

    private static final Logger LOG = Logger.getLogger(MinimumWindowSubstring.class);

    private MinimumWindowSubstring() throws Exception {

        String str = "aa";
        String pattern = "aa";

        System.out.println(minWindow(str, pattern));

        System.out.println("MinimumWindowSubstring done...");
    }

    public static void main(String[] args) {
        try {
            new MinimumWindowSubstring();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private Map<Character, Count> createMap(String pattern) {

        Map<Character, Count> map = new HashMap<>();

        for (int i = 0, length = pattern.length(); i < length; i++) {
            char ch = pattern.charAt(i);

            if (map.containsKey(ch)) {
                map.get(ch).expected += 1;
            }
            else {
                map.put(ch, new Count(1, 0));
            }
        }

        return map;
    }

    /**
     * time: O(N)
     * space: O(M)
     */
    public String minWindow(String s, String t) {

        if (t.length() > s.length()) {
            return "";
        }

        Map<Character, Count> map = createMap(t);

        int from = 0;
        int to = -1;

        int minFrom = -1;
        int minTo = -1;

        int minWindowLength = Integer.MAX_VALUE;

        int matchedCount = 0;

        while (true) {

            if (matchedCount == t.length()) {

                int curLength = to - from + 1;

                if (curLength < minWindowLength) {
                    minWindowLength = curLength;

                    minFrom = from;
                    minTo = to;
                }

                // try to reduce window
                char chToRemove = s.charAt(from);
                ++from;

                Count count = map.get(chToRemove);

                if (count != null) {

                    count.actual -= 1;

                    if (count.actual < count.expected) {
                        matchedCount -= 1;
                    }
                }
            }
            else {

                if (to == s.length() - 1) {
                    break;
                }

                // try to increase window
                ++to;
                char charToAdd = s.charAt(to);

                Count count = map.get(charToAdd);

                if (count != null) {
                    count.actual += 1;

                    if (count.actual <= count.expected) {
                        matchedCount += 1;
                    }
                }
            }
        }

        return minFrom >= 0 ? s.substring(minFrom, minTo + 1) : "";
    }

    static class Count {

        int expected;
        int actual;

        public Count(int expected, int actual) {
            this.expected = expected;
            this.actual = actual;
        }

        @Override
        public String toString() {
            return "expected: " + expected + ", actual: " + actual;
        }
    }
}
