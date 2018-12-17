package com.max.algs.string;


import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Global alignment Needleman-Wunsch algorithm (a.k.a edit distance).
 * See wiki: https://en.wikipedia.org/wiki/Needleman%E2%80%93Wunsch_algorithm
 */
final class EditDistance {

    private EditDistance() {
        throw new IllegalStateException("Can't initialize utility only class");
    }

    /**
     * Check if String 'str' can be converted to 'other' string using at most 'd'
     * transformations (insertion, deletion, replacement).
     * <p>
     * <p>
     * N - str.length()
     * M - other.length()
     * <p>
     * time: O(d * min(N, M))
     * space: O(d)
     */
    static boolean canEditWithDistance(String str, String other, int d) {
        checkArgument(str != null, "null 'str' passed");
        checkArgument(other != null, "null 'other' passed");
        checkArgument(d >= 0, "negative distance passed");

        if (d == 0) {
            return str.equals(other);
        }

        if (Math.abs(str.length() - other.length()) > d) {
            return false;
        }

        if (Math.max(str.length(), other.length()) <= d) {
            return true;
        }

        if (str.length() >= other.length()) {
            return canEditWithDistanceInternal(str, other, d);
        }

        return canEditWithDistanceInternal(other, str, d);
    }


    private static boolean canEditWithDistanceInternal(String str, String other, int d) {

        final int rows = other.length() + 1;
        final int cols = (2 * d) + 3;
        final int median = d + 1;

        int[] prev = new int[cols];

        Arrays.fill(prev, Integer.MAX_VALUE);
        for (int i = median, index = 0; i < prev.length - 1; ++i, ++index) {
            prev[i] = index;
        }

        for (int rowIndex = 1, colIndex = 1; rowIndex < rows; ++rowIndex, ++colIndex) {
            int[] cur = new int[cols];
            Arrays.fill(cur, Integer.MAX_VALUE);

            char otherCh = other.charAt(rowIndex - 1);

            int colOffset = colIndex - median;

            for (int i = 1; i < cur.length - 1; ++i) {

                int realCol = i + colOffset;

                if (realCol == 0) {
                    cur[i] = rowIndex <= d ? rowIndex : Integer.MAX_VALUE;
                }
                else if (realCol > 0 && realCol <= str.length()) {

                    char strCh = str.charAt(realCol - 1);

                    if (strCh == otherCh) {
                        cur[i] = prev[i];
                    }
                    else {
                        cur[i] = 1 + min(prev[i], prev[i + 1], cur[i - 1]);
                    }
                }
            }

            prev = cur;
        }

        if (prev[median] == Integer.MAX_VALUE) {
            return false;
        }

        return prev[median] <= d;
    }

    static int editDistance(String str, String other) {

        checkArgument(str != null, "null 'str' passed");
        checkArgument(other != null, "null 'other' passed");

        if (str.length() >= other.length()) {
            return editDistanceInternal(str, other);
        }

        return editDistanceInternal(other, str);
    }

    /**
     * Levenshtein edit distance.
     * <p>
     * N - str.length()
     * M - other.length()
     * <p>
     * time: O(N*M)
     * space: O( min(N, M) )
     */
    private static int editDistanceInternal(String str, String other) {

        final int rows = other.length() + 1;
        final int cols = str.length() + 1;

        int[] prev = new int[cols];

        for (int colIndex = 1; colIndex < cols; colIndex++) {
            prev[colIndex] = colIndex;
        }

        for (int rowIndex = 1; rowIndex < rows; ++rowIndex) {

            int[] cur = new int[cols];
            cur[0] = rowIndex;

            for (int colIndex = 1; colIndex < cols; ++colIndex) {
                char strCh = str.charAt(colIndex - 1);
                char otherCh = other.charAt(rowIndex - 1);

                if (strCh == otherCh) {
                    cur[colIndex] = prev[colIndex - 1];
                }
                else {
                    cur[colIndex] = 1 + min(prev[colIndex], cur[colIndex - 1], prev[colIndex - 1]);
                }
            }

            prev = cur;
        }

        return prev[cols - 1];
    }

    private static int min(int first, int second, int third) {
        return Math.min(Math.min(first, second), third);
    }

}
