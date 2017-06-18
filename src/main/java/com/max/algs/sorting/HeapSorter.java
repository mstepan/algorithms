package com.max.algs.sorting;

import com.max.algs.util.ArrayUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;

public final class HeapSorter {

    private static final Logger LOG = Logger.getLogger(HeapSorter.class);

    private HeapSorter() {
        super();
    }

    /**
     * time: O(N*lgN) space: O(1)
     * <p>
     * In-place heapsort of an array.
     */
    public static void sort(int[] arr) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Original:" + Arrays.toString(arr));
        }

        heapify(arr);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Heap:" + Arrays.toString(arr));
        }

        int length = arr.length;

        while (length > 1) {
            --length;
            ArrayUtils.fastSwap(arr, 0, length);
            fixDown(arr, 0, length);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Sorted:" + Arrays.toString(arr));
        }
    }

    /**
     * time: O(N) space: O(1)
     * <p>
     * Create from unordered array max binary heap.
     */
    public static void heapify(int[] arr) {
        for (int i = arr.length / 2; i >= 0; i--) {
            fixDown(arr, i, arr.length);
        }
    }

    private static void fixDown(int[] arr, int index, int length) {

        int left = -1;
        int right = -1;
        int minIndex = -1;

        while (true) {

            left = (index << 1) | 1;
            right = (index << 1) + 2;
            minIndex = index;

            if (left < length && arr[left] > arr[minIndex]) {
                minIndex = left;
            }

            if (right < length && arr[right] > arr[minIndex]) {
                minIndex = right;
            }

            if (index == minIndex) {
                break;
            }

            ArrayUtils.fastSwap(arr, index, minIndex);
            index = minIndex;
        }

    }

    public static void main(String[] args) {

        long percentageSum = 0;
        int itCount = 10;

        for (int i = 0; i < itCount; i++) {
            int[] arr = ArrayUtils.generateRandomArray(1_000_000);

            int[] floydArr = Arrays.copyOf(arr, arr.length);
            int[] res = Arrays.copyOf(arr, arr.length);

            long startTime = System.currentTimeMillis();
            HeapSorter.sort(arr);
            long offset = System.currentTimeMillis() - startTime;

            long startTimeFloyd = System.currentTimeMillis();
            FloydHeapSorter.sort(floydArr);
            long offsetFloyd = System.currentTimeMillis() - startTimeFloyd;

            long floydsPercentage = offsetFloyd * 100 / offset;
            percentageSum += 100 - floydsPercentage;

            Arrays.sort(res);

            if (!Arrays.equals(res, arr)) {
                throw new IllegalStateException(
                        "Classic heapsort isn't working properly, array isn't sorted");
            }

            if (!Arrays.equals(res, floydArr)) {
                throw new IllegalStateException(
                        "Floyds heapsort isn't working properly, array isn't sorted");
            }

            LOG.info(i + " finished");
        }

        LOG.info("Avg speedup: " + (percentageSum / itCount) + " %");

    }
}
