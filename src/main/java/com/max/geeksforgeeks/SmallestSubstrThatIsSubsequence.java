package com.max.geeksforgeeks;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;


/**
 * Given two strings A and B, the task is to find the smallest substring of A having B as a subsequence.
 * If there are multiple such substrings in A, return the substring that has the smallest starting index.
 * <p>
 * https://www.geeksforgeeks.org/find-length-of-smallest-substring-of-a-given-string-which-contains-another-string-as
 * -subsequence/
 * <p>
 * N - a.length()
 * M - b.length()
 * <p>
 * time: O(N*M)
 * space: O(N*M), can be reduced to O(N) because we only need one previous row
 */
public class SmallestSubstrThatIsSubsequence {

    private static Optional<String> findShortestSubstring(String aStr, String bStr) {
        Objects.requireNonNull(aStr, "NULL 'aStr' detected");
        Objects.requireNonNull(bStr, "NULL 'bStr' detected");

        // handle few corner cases
        if (aStr.length() < bStr.length()) {
            return Optional.empty();
        }

        if (aStr.length() == bStr.length() && aStr.equals(bStr)) {
            return Optional.of(aStr);
        }

        // main algorithm below
        final char[] a = aStr.toCharArray();
        final char[] b = bStr.toCharArray();

        final int rows = b.length;
        final int cols = a.length;

        final int[][] res = new int[rows][cols];

        // fill res with all -1
        fill(res, -1);

        // fill in 1-st row
        for (int col = 0; col < cols; ++col) {
            if (a[col] == b[0]) {
                res[0][col] = col;
            }
            else {
                res[0][col] = (col > 0) ? res[0][col - 1] : -1;
            }
        }

        // fill main part
        for (int row = 1; row < rows; ++row) {
            for (int col = 1; col < cols; ++col) {
                if (b[row] == a[col]) {
                    res[row][col] = res[row - 1][col - 1];
                }
                else {
                    res[row][col] = res[row][col - 1];
                }
            }
        }

        // calculate solution from last row
        int startPos = -1;
        int minLength = Integer.MAX_VALUE;

        final int lastRow = rows - 1;

        for (int col = 1; col < cols; ++col) {
            if (res[lastRow][col] >= 0) {
                int curLength = col - res[lastRow][col] + 1;
                if (curLength < minLength) {
                    minLength = curLength;
                    startPos = res[lastRow][col];
                }
            }
        }

        return startPos == -1 ? Optional.empty() : Optional.of(aStr.substring(startPos, startPos + minLength));
    }

    private static void fill(int[][] res, int initValue) {
        for (int[] row : res) {
            Arrays.fill(row, initValue);
        }
    }

}
