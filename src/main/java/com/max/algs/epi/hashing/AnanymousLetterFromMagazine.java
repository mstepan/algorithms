package com.max.algs.epi.hashing;


import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public final class AnanymousLetterFromMagazine {

    private static final Logger LOG = Logger.getLogger(AnanymousLetterFromMagazine.class);

    private static final Integer ONE_INTEGER = 1;

    private AnanymousLetterFromMagazine() throws Exception {

        String letter = "hello world";
        String[] magazines = {"help at work. helldiver big o", "help at work. helldiver big O"};

        for (String magazine : magazines) {
            System.out.printf("canConstructLetterFromMagazine: %b %n",
                    canConstructLetterFromMagazine(letter, magazine));
        }

        System.out.printf("AnanymousLetterFromMagazine: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * M - letter.length()
     * N - magazine.length()
     * <p>
     * time: O(M+N)
     * space: O(M)
     */
    public static boolean canConstructLetterFromMagazine(String letter, String magazine) {
        checkNotNull(letter);
        checkNotNull(magazine);

        if (letter.length() > magazine.length()) {
            return false;
        }

        Map<Character, Integer> letterCharFreq = new HashMap<>();

        for (int i = 0, length = letter.length(); i < length; ++i) {
            letterCharFreq.compute(letter.charAt(i), (chKey, freqVal) -> freqVal == null ? 1 : freqVal + 1);
        }

        for (int i = 0, length = magazine.length(); i < length; ++i) {
            char ch = magazine.charAt(i);

            Integer chFreq = letterCharFreq.get(ch);

            if (chFreq != null) {
                if (chFreq.equals(ONE_INTEGER)) {
                    letterCharFreq.remove(ch);
                }
                else {
                    letterCharFreq.put(ch, chFreq - 1);
                }

                if (letterCharFreq.isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
        try {
            new AnanymousLetterFromMagazine();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}

