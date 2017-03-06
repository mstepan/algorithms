package com.max.algs.epi.string.matching;

import com.max.algs.string.StringUtils;

import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;


public class RabinKarpStringMatching {

    private static final int BASE = 257;
    private static final int BIG_PRIME = 7_368_787;

    static {
        // special care should be taken here, because 'signature * BASE' can overflow.
        // signature = (signature * BASE) % BIG_PRIME;
        long res = ((long) (BASE)) * BIG_PRIME - 1;

        if (res > Integer.MAX_VALUE) {
            throw new ExceptionInInitializerError("'BASE * (BIG_PRIME-1)' can overflow: " + res);
        }
    }

    /**
     * Rabin-Karp substring exact matching algorithm.
     * <p>
     * time: O(N*M), avg will be O(N+M).
     * space: O(1), because we use modulo arithmetic.
     */
    public static int find(String str, String pattern) {
        checkArgument(str != null, "null 'str' argument passed");
        checkArgument(pattern != null, "null 'pattern' argument passed");
        checkArgument(str.length() >= pattern.length(), "str.length < pattern.length: %s < %s",
                      str.length(), pattern.length());

        final int strLength = str.length();
        final int patternLength = pattern.length();

        int pow = calculatePowerMultiplier(patternLength);

        int patternSignature = calculateSignature(pattern, 0, patternLength - 1);
        int strSignature = calculateSignature(str, 0, patternLength - 1);

        //match at '0' position
        if (patternSignature == strSignature && isMatched(str, pattern, 0)) {
            return 0;
        }

        for (int i = 1; i <= strLength - patternLength; ++i) {

            strSignature = recalculateSignature(str, strSignature, i, patternLength, pow);

            //match
            if (patternSignature == strSignature && isMatched(str, pattern, i)) {
                return i;
            }
        }

        return -1;
    }

    private static int calculatePowerMultiplier(int patternLength) {
        int pow = 1;

        for (int i = 0; i < patternLength - 1; ++i) {
            pow = (pow * BASE) % BIG_PRIME;
        }

        return pow;
    }

    private static boolean isMatched(String str, String pattern, int offset) {
        for (int i = 0; i < pattern.length(); ++i) {
            if (pattern.charAt(i) != str.charAt(i + offset)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Recalculate string signature after one digit shifted to the right using module arithmetic .
     */
    private static int recalculateSignature(String str, int signature, int index, int length, int pow) {

        int oldDigit = str.charAt(index - 1);
        int newDigit = str.charAt(index + length - 1);

        // remove old digit value (most significant position)
        signature = signature - (((oldDigit * pow)) % BIG_PRIME);

        // fix negative value
        if (signature < 0) {
            signature += BIG_PRIME;
        }

        // shift one BASE position to the left
        signature = (signature * BASE) % BIG_PRIME;

        // add new digit value
        signature = (signature + newDigit) % BIG_PRIME;

        return signature;
    }

    /**
     * Use Horner's rule to calculate polynom value.
     */
    private static int calculateSignature(String str, int from, int to) {

        int signature = 0;

        for (int i = from; i <= to; ++i) {
            signature = ((signature * BASE) % BIG_PRIME + str.charAt(i)) % BIG_PRIME;
        }

        return signature;
    }

    private RabinKarpStringMatching() throws Exception {

        Random rand = new Random();

        for (int i = 0; i < 1_000; ++i) {
            String str = StringUtils.generateDNAString(20 + rand.nextInt(5000));
            String pattern = StringUtils.generateDNAString(3 + rand.nextInt(10));

            int actualIndex = find(str, pattern);
            int expectedIndex = str.indexOf(pattern);

//            System.out.printf("actualIndex = %d, expectedIndex = %d %n", actualIndex, expectedIndex);

            if (actualIndex != expectedIndex) {
                System.out.printf("actualIndex = %d, expectedIndex = %d %n", actualIndex, expectedIndex);
                throw new IllegalStateException("actualIndex != expectedIndex for string '" + str + "' and pattern '" +
                                                        pattern + "'");
            }
        }


        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new RabinKarpStringMatching();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
