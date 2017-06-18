package com.max.algs.sorting;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public final class MergeSorter {

    /**
     * If the length of an array to be sorted is less than this constant,
     * insertion sort is used.
     */
    private static final int INSERTION_SORT_THRESHOLD = 47;
    private static final int PROCESSORS_COUNT = Runtime.getRuntime()
            .availableProcessors();

    private MergeSorter() {
        super();
    }

    public static void parallelSort(int[] arr) {
        ForkJoinPool pool = new ForkJoinPool(PROCESSORS_COUNT);
        pool.invoke(new MergeSortTask(arr, 0, arr.length - 1));
    }

    /**
     * Implement recursive mergesort.
     * <p>
     * Time: O(N*lgN) Space: O(N)
     */
    public static void sort(int[] arr) {
        mergeRecursively(arr, 0, arr.length - 1);
    }

    private static void mergeRecursively(int[] arr, int from, int to) {

        if ((to - from + 1) < INSERTION_SORT_THRESHOLD) {
            insertionSort(arr, from, to);
            return;
        }

        int middle = (from + to) >>> 1;

        mergeRecursively(arr, from, middle);
        mergeRecursively(arr, middle + 1, to);

        mergeSorted(arr, from, middle, to);
    }

    private static void insertionSort(int[] arr, int from, int to) {

        for (int i = from + 1; i <= to; i++) {

            int temp = arr[i];

            int j = i - 1;

            while (j >= from && arr[j] > temp) {
                arr[j + 1] = arr[j];
                --j;
            }

            arr[j + 1] = temp;
        }

    }

    private static void mergeSorted(int[] arr, int from, int middle, int to) {

        int[] arr1 = new int[middle - from + 1 + 1];
        System.arraycopy(arr, from, arr1, 0, arr1.length - 1);
        arr1[arr1.length - 1] = Integer.MAX_VALUE;

        int[] arr2 = new int[to - (middle + 1) + 1 + 1];
        System.arraycopy(arr, middle + 1, arr2, 0, arr2.length - 1);
        arr2[arr2.length - 1] = Integer.MAX_VALUE;

        int elemsCount = to - from + 1;

        int index1 = 0;
        int index2 = 0;

        int baseIndex = from;

        while (elemsCount > 0) {
            if (arr1[index1] <= arr2[index2]) {
                arr[baseIndex] = arr1[index1++];
            }
            else {
                arr[baseIndex] = arr2[index2++];
            }

            ++baseIndex;
            --elemsCount;
        }
    }

    private static final class MergeSortTask extends RecursiveTask<Void> {

        private static final long serialVersionUID = 4700360510743306538L;

        private final int[] arr;
        private final int from;
        private final int to;

        public MergeSortTask(int[] arr, int from, int to) {
            super();
            this.arr = arr;
            this.from = from;
            this.to = to;
        }

        @Override
        protected Void compute() {

            int elems = to - from + 1;

            if (elems < INSERTION_SORT_THRESHOLD) {
                SortUtils.insertionSort(arr, from, to);
                return null;
            }

            int middle = (from + to) >>> 1;

            RecursiveTask<Void> left = new MergeSortTask(arr, from, middle);
            left.fork();

            RecursiveTask<Void> right = new MergeSortTask(arr, middle + 1, to);
            right.fork();

            left.join();
            right.join();

            SortUtils.merge(arr, from, middle, middle + 1, to);

            return null;
        }

    }

}
