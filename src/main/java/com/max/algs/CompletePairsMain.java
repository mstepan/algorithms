package com.max.algs;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class CompletePairsMain {


    private static final int MASK_FOR_26_BITS = 0x03_FF_FF_FF;
    private static final char FIRST_CH = 'a';
    private static final char LAST_CH = 'z';
    private CompletePairsMain() throws Exception {

        String[] s1 = {"abcdefgh", "geeksforgeeks", "lmnopqrst", "abc"};
        String[] s2 = {"ijklmnopqrstuvwxyz", "abcdefghijklmnopqrstuvwxyz", "defghijklmnopqrstuvwxyz"};

        int cnt = completePairs(s1, s2);

        System.out.printf("cnt = %d %n", cnt);

        System.out.printf("CompletePairsMain done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * time: O(N^2)
     * space: O(N)
     */
    public static int completePairs(String[] s1, String[] s2) {

        checkNotNull(s1);
        checkNotNull(s2);

        int[] bitMap1 = calculateBitMaps(s1);
        int[] bitMap2 = calculateBitMaps(s2);

        int completePairsCnt = 0;

        for (int i = 0; i < bitMap1.length; ++i) {

            int hash1 = bitMap1[i];

            for (int j = 0; j < bitMap2.length; ++j) {
                int hash2 = bitMap2[j];

                if ((hash1 | hash2) == MASK_FOR_26_BITS) {
                    System.out.printf("%s %s %n", s1[i], s2[j]);
                    ++completePairsCnt;
                }
            }
        }

        return completePairsCnt;
    }

    private static int[] calculateBitMaps(String[] arr) {
        int[] hashes = new int[arr.length];

        for (int i = 0; i < arr.length; ++i) {
            hashes[i] = calculateBitHash(arr[i], FIRST_CH, LAST_CH);
        }
        return hashes;
    }

    private static int calculateBitHash(String str, char firstCh, char lastCH) {
        assert str != null;

        int hash = 0;

        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);

            checkArgument(ch >= firstCh && ch <= lastCH, "Character '%s' not from alphabet: '%s'...'%s'",
                    ch, firstCh, lastCH);

            hash = hash | (1 << (str.charAt(i) - firstCh));
        }

        return hash;
    }

    public static void main(String[] args) {
        try {
            new CompletePairsMain();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

