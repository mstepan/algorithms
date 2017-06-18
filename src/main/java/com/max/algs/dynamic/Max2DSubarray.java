package com.max.algs.dynamic;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Find biggest rectangle subarray with all '1'
 */
public final class Max2DSubarray {

    private Max2DSubarray() {
        throw new IllegalArgumentException("Can't call constructor for non instantiable class '" +
                Max2DSubarray.class + "'");
    }

    /**
     * time: O(N^3)
     * space: O(N)
     */
    public static int findMaxSubarray(@Nonnull int[][] matrix) {

        checkArgument(matrix != null, "null 'matrix' passed");

        int[] rowHeight = new int[matrix[0].length];
        int maxSquare = 0;

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {

                if (matrix[row][col] == 1) {

                    rowHeight[col] += 1;

                    int minHeight = Integer.MAX_VALUE;

                    for (int k = col; k >= 0 && matrix[row][k] != 0; k--) {
                        minHeight = Math.min(minHeight, rowHeight[k]);

                        int curSquare = minHeight * (col - k + 1);

                        maxSquare = Math.max(maxSquare, curSquare);
                    }
                }
                else {
                    rowHeight[col] = 0;
                }
            }

        }

        return maxSquare;
    }
}
