package com.max.algs;


import java.util.HashMap;
import java.util.Map;

import static org.parboiled.common.Preconditions.checkNotNull;

public final class AlgorithmsMain {


    /**
     * time: O(N)
     * space: O(N)
     */
    private static int countSubstrings(String str) {
        checkNotNull(str);

        if (str.length() < 2) {
            return str.length();
        }

        Map<Character, Integer> freq = new HashMap<>();
        int cnt = str.length();

        for (int i = 0, strLength = str.length(); i < strLength; ++i) {
            char ch = str.charAt(i);

            Integer curChCount = freq.computeIfAbsent(ch, key -> 0);
            cnt += curChCount;

            freq.put(ch, curChCount + 1);
        }

        return cnt;
    }

    private AlgorithmsMain() throws Exception {

        String str = "abcab";

        System.out.println(countSubstrings(str));

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new AlgorithmsMain();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

