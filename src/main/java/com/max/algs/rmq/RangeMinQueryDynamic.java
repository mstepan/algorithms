package com.max.algs.rmq;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Range minimum query DS using dynamic approach.
 */
public final class RangeMinQueryDynamic {

    private final int[] originalArr;
    private final int[][] data;

    public RangeMinQueryDynamic(int[] arr) {
        this.originalArr = arr;
        this.data = createResult(arr);
    }

    /**
     * time: O(1)
     */
    public int minIndex(int from, int to) {
        checkArgument(from <= to, "Incorrect from and/or to values");
        return data[from][to];
    }

    /**
     * time: O(1)
     */
    public int minValue(int from, int to) {
        return originalArr[minIndex(from, to)];
    }

    /**
     * time: O(N^2)
     * space: O(N^2)
     */
    private int[][] createResult(int[] arr) {

        int[][] res = new int[arr.length][arr.length];

        for (int i = 0; i < arr.length; ++i) {
            res[i][i] = i;
        }

        for (int i = 0; i < arr.length - 1; ++i) {
            for (int j = i + 1; j < arr.length; ++j) {

                int prevValue = arr[res[i][j - 1]];
                int curValue = arr[j];

                if (prevValue < curValue) {
                    res[i][j] = res[i][j - 1];
                }
                else {
                    res[i][j] = j;
                }
            }
        }

        return res;
    }
}
