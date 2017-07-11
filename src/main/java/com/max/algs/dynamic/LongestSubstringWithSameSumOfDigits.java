package com.max.algs.dynamic;


import com.max.algs.string.StringUtils;
import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Find longest substring with the same sum of digits for left and right halves.
 * <p>
 * Dynamic and brute force solutions.
 */
public final class LongestSubstringWithSameSumOfDigits {

    private static final Logger LOG = Logger.getLogger(LongestSubstringWithSameSumOfDigits.class);

    /**
     * time: O(N^3)
     * space: O(1)
     */
    private static String findMaxSubstringBruteforce(String str) {
        checkNotNull(str);

        int maxLength = 0;
        String maxSub = "";

        for (int from = 0; from < str.length() - 1; ++from) {
            for (int to = from + 1; to < str.length(); ++to) {

                int length = to - from + 1;

                if ((length & 1) == 0 && length > maxLength && isSameSumHalves(str.substring(from, to + 1))) {
                    maxLength = length;
                    maxSub = str.substring(from, to + 1);
                }
            }
        }

        return maxSub;
    }

    private static boolean isSameSumHalves(String str) {

        int leftSum = 0;
        for (int i = 0; i < str.length() / 2; ++i) {
            leftSum += toDigit(str.charAt(i));
        }

        int rightSum = 0;
        for (int j = str.length() / 2; j < str.length(); ++j) {
            rightSum += toDigit(str.charAt(j));
        }

        return leftSum == rightSum;
    }

    /**
     * time: O(N^2)
     * space: O(N^2)
     */
    private static String findMaxSubstring(String str) {
        checkNotNull(str);

        int n = str.length();

        int[][] sum = new int[n][n];

        for (int i = 0; i < n; ++i) {
            sum[i][i] = toDigit(str.charAt(i));
        }

        String maxSub = "";
        int maxLength = 0;

        for (int row = n - 2; row >= 0; --row) {
            for (int col = row + 1; col < n; ++col) {

                sum[row][col] = sum[row][col - 1] + toDigit(str.charAt(col));

                int length = col - row + 1;

                // consider even length only
                if ((length & 1) == 0 && length >= maxLength) {

                    int mid = row + (length >> 1) - 1;

                    if (sum[row][mid] == sum[mid + 1][col]) {
                        maxLength = length;
                        maxSub = str.substring(row, col + 1);
                    }
                }
            }
        }

        return maxSub;
    }

    /**
     * time: O(N^2)
     * space: O(1)
     */
    private static String findMaxSubstringOptimal(String str) {
        checkNotNull(str);

        int n = str.length();

        String maxSub = "";
        int maxLength = 0;


        for (int from = 0; from < n - 1; ++from) {

            int smallPrefix = toDigit(str.charAt(from));
            int fullPrefix = smallPrefix + toDigit(str.charAt(from + 1));

            int leftSum = smallPrefix;
            int rightSum = fullPrefix - smallPrefix;

            if (leftSum == rightSum && 2 > maxLength) {
                maxLength = 2;
                maxSub = str.substring(from, from + 2);
            }

            int lastLeft = from + 1;

            for (int to = from + 3; to < n; to += 2, ++lastLeft) {

                fullPrefix += toDigit(str.charAt(to));
                fullPrefix += toDigit(str.charAt(to - 1));

                int length = to - from + 1;

                smallPrefix += toDigit(str.charAt(lastLeft));

                leftSum = smallPrefix;
                rightSum = fullPrefix - smallPrefix;

                if (leftSum == rightSum && length > maxLength) {
                    maxLength = length;
                    maxSub = str.substring(from, to + 1);
                }
            }
        }

        return maxSub;
    }

    private static int toDigit(char ch) {
        return ch - '0';
    }

    private LongestSubstringWithSameSumOfDigits() {

        String str = StringUtils.generateDigitsString(20);

        LOG.info("str: " + str);

        LOG.info("maxSub: " + findMaxSubstringBruteforce(str));
        LOG.info("maxSub: " + findMaxSubstring(str));
        LOG.info("maxSub: " + findMaxSubstringOptimal(str));

        LOG.info("LongestSubstringWithSameSumOfDigits: java-" + System.getProperty("java.version"));
    }


    public static void main(String[] args) {
        try {
            new LongestSubstringWithSameSumOfDigits();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}

