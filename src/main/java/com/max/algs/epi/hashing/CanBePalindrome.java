package com.max.algs.epi.hashing;


import com.max.algs.string.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public final class CanBePalindrome {

    private static final Logger LOG = Logger.getLogger(CanBePalindrome.class);

    private CanBePalindrome() throws Exception {

        String[] arr = {"racecar", "hello, world", "anpna", "anpsna"};

        for (String val : arr) {
            String str = StringUtils.randomShuffle(val);
            boolean canBePalindrome = canBeShuffledToPalindrome(str);

            System.out.printf("str: '%s', can be palindrome: %b %n", str, canBePalindrome);
        }

        System.out.printf("CanBePalindrome: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * time: O(N)
     * space: O(N)
     */
    public static boolean canBeShuffledToPalindrome(String str) {
        checkNotNull(str);

        if (str.length() < 2) {
            return true;
        }

        Map<Character, Integer> parityMap = new HashMap<>();

        for (int i = 0, length = str.length(); i < length; ++i) {
            parityMap.compute(str.charAt(i), (key, val) -> val == null ? 1 : val ^ 1);
        }

        int oddChars = countOddChars(parityMap);

        // even length
        if ((str.length() & 1) == 0) {
            return oddChars == 0L;
        }

        // odd length
        return oddChars == 1L;
    }

    private static int countOddChars(Map<Character, Integer> parityMap) {
//        return parityMap.values().stream().
//                filter(val -> val == 1).mapToInt(val -> 1).sum();

        int oddChars = 0;

        for (Integer chParity : parityMap.values()) {
            if (chParity == 1) {
                ++oddChars;
            }
        }

        return oddChars;
    }

    public static void main(String[] args) {
        try {
            new CanBePalindrome();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}

