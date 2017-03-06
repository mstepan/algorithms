package com.max.algs.sorting.tim;

import java.util.Arrays;

/**
 * Single tim sort run.
 */
final class Run {

    final int[] arr;
    final int from;
    final int to;

    Run(int[] arr, int from, int to) {
        this.arr = arr;
        this.from = from;
        this.to = to;
    }

    int length() {
        return to - from + 1;
    }

    boolean isSorted() {

        for (int i = from + 1; i <= to; i++) {
            if (arr[i - 1] > arr[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "[" + from + ", " + to + "]: sorted = " + isSorted() +
                " -> " + Arrays.toString(Arrays.copyOfRange(arr, from, to + 1));
    }
}
