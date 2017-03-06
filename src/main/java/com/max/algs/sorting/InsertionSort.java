package com.max.algs.sorting;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Insertion sort.
 */
public class InsertionSort {


    public static void sort(int[] arr) {

        checkNotNull(arr);

        final int arrLength = arr.length;
        int j;
        int temp;

        for (int i = 1; i < arrLength; ++i) {
            j = i - 1;
            temp = arr[i];

            while (j >= 0 && arr[j] > temp) {
                arr[j + 1] = arr[j];
                --j;
            }

            arr[j + 1] = temp;
        }
    }

    public static void sortWithSentinel(int[] arr) {

        checkNotNull(arr);

        final int arrLength = arr.length;

        int minIndex = 0;
        int minValue = arr[0];

        for (int i = 1; i < arr.length; ++i) {
            if (arr[i] < minValue) {
                minIndex = i;
                minValue = arr[i];
            }
        }

        swap(arr, 0, minIndex);

        int j;
        int temp;

        for (int i = 1; i < arrLength; ++i) {
            j = i - 1;
            temp = arr[i];

            while (arr[j] > temp) {
                arr[j + 1] = arr[j];
                --j;
            }

            arr[j + 1] = temp;
        }
    }

    private static void swap(int[] arr, int from, int to) {
        int temp = arr[from];
        arr[from] = arr[to];
        arr[to] = temp;
    }

}
