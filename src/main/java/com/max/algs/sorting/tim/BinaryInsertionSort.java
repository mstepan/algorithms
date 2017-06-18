package com.max.algs.sorting.tim;

/**
 * Binary insertion sort.
 * This sort is stable.
 */
final class BinaryInsertionSort {

    private BinaryInsertionSort() {
    }

    public static void sort(int[] arr, int baseFrom, int baseTo) {

        for (int i = baseFrom + 1; i <= baseTo; i++) {
            int temp = arr[i];

            int from = baseFrom;
            int to = i - 1;

            while (from <= to) {
                int mid = from + ((to - from) >> 1);

                if (arr[mid] > temp) {
                    to = mid - 1;
                }
                else {
                    from = mid + 1;
                }
            }

            int lastIndex = to + 1;

            if (lastIndex != i) {
                System.arraycopy(arr, lastIndex, arr, lastIndex + 1, i - lastIndex);
                arr[lastIndex] = temp;
            }
        }
    }
}
