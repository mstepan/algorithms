package com.max.algs.string;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Manacher's algorithm to find the longest palindromic substring.
 * See https://articles.leetcode.com/longest-palindromic-substring-part-ii/
 * <p>
 * time: O(2N)
 * space: O(N)
 */
final class LongestPalindromicSubstring {

    private static final char DELIMITER_CHAR = '#';

    /**
     * time: O(N^3)
     * space: O(1)
     */
    static String findBruteforce(String str) {

        int maxLength = 1;
        int start = 0;
        int end = 0;

        final int strLength = str.length();

        for (int from = 0; from < strLength - 1; ++from) {
            for (int to = from + 1; to < strLength; ++to) {
                if (isPalindrome(str, from, to) && (to - from + 1) > maxLength) {
                    maxLength = (to - from + 1);

                    start = from;
                    end = to;
                }
            }
        }

        return str.substring(start, end + 1);
    }

    private static boolean isPalindrome(String str, int from, int to) {
        for (int left = from, right = to; left < right; ++left, --right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
        }
        return true;
    }

    static String find(String str) {
        checkNotNull(str, "null 'str' passed");

        if (str.length() < 2) {
            return str;
        }

        char[] arr = createStr(str);
        int[] palindrome = new int[arr.length];
        palindrome[1] = 1;

        int center = 1;
        int right = 2;

        while (true) {

            center = moveCenter(center, palindrome, right);

            if (center >= arr.length) {
                break;
            }

            int left = center - palindrome[center];
            right = center + palindrome[center];

            int curLength = palindrome[center] - 1;

            while (left >= 0 && right < arr.length && arr[left] == arr[right]) {
                --left;
                ++right;

                ++curLength;
            }
            --right;

            palindrome[center] = curLength;
        }

        return createLongestPalindrome(palindrome, arr);
    }

    private static String createLongestPalindrome(int[] palindrome, char[] arr) {

        int maxIndex = 0;

        for (int i = 1; i < palindrome.length; ++i) {
            if (palindrome[i] > palindrome[maxIndex]) {
                maxIndex = i;
            }
        }

        final int center = maxIndex;
        final int palindromeLength = palindrome[center];

        StringBuilder leftBuf = new StringBuilder(palindromeLength);
        for (int i = center - 1; i >= center - palindromeLength; --i) {
            if (arr[i] != DELIMITER_CHAR) {
                leftBuf.append(arr[i]);
            }
        }

        // construct right side
        StringBuilder rightBuf = new StringBuilder(palindromeLength);
        for (int i = center + 1; i <= center + palindromeLength; ++i) {
            if (arr[i] != DELIMITER_CHAR) {
                rightBuf.append(arr[i]);
            }
        }

        StringBuilder res = new StringBuilder();

        res.append(leftBuf.reverse());
        if (arr[center] != DELIMITER_CHAR) {
            res.append(arr[center]);
        }
        res.append(rightBuf);

        return res.toString();
    }

    private static int moveCenter(int center, int[] palindrome, int right) {

        for (int i = center + 1, j = center - 1; i <= right; ++i, --j) {

            int curPalindromeLength = palindrome[j];

            if (i + curPalindromeLength < right) {
                palindrome[i] = curPalindromeLength;
            }
            else {
                // new center found
                palindrome[i] = right - i;
                return i;
            }
        }

        return right + 1;
    }

    private static char[] createStr(String str) {
        char[] arr = new char[str.length() * 2 + 1];

        for (int i = 0, index = 0; i < arr.length; ++i) {
            // even index
            if ((i & 1) == 0) {
                arr[i] = DELIMITER_CHAR;
            }
            // odd index
            else {
                arr[i] = str.charAt(index);
                ++index;
            }
        }

        return arr;
    }

    private LongestPalindromicSubstring() {
        throw new IllegalStateException("Can't instantiate utility only class");
    }
}
