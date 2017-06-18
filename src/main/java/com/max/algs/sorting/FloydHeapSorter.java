package com.max.algs.sorting;

import com.max.algs.util.ArrayUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;

public final class FloydHeapSorter {

    private static final Logger LOG = Logger.getLogger(FloydHeapSorter.class);

    private FloydHeapSorter() {
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

        HeapSorter.heapify(arr);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Heap:" + Arrays.toString(arr));
        }

        int length = arr.length;

        while (length > 1) {

            int largest = arr[0];
            int index = 0;
            int left;
            int right;
            int indexShiftedByOne;

            while (index < length / 2) {

                indexShiftedByOne = (index << 1) | 1;

                left = indexShiftedByOne;
                right = indexShiftedByOne + 1;

                // right is max
                if (right < length && arr[right] > arr[left]) {
                    arr[index] = arr[right];
                    index = right;
                }
                // left is max
                else {
                    arr[index] = arr[left];
                    index = left;
                }
            }

            --length;

            if (index == length) {
                arr[index] = largest;
            }
            else {
                arr[index] = arr[length];
                arr[length] = largest;
                fixUp(arr, index);
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Sorted:" + Arrays.toString(arr));
        }
    }

    private static void fixUp(int[] arr, int index) {

        while (index > 0) {
            int parent = parentIndex(index);

            if (arr[parent] > arr[index]) {
                break;
            }

            ArrayUtils.fastSwap(arr, parent, index);
            index = parent;
        }
    }

    private static int parentIndex(int index) {

        // 'even' index
        if ((index & 1) == 0) {
            return (index >>> 1) - 1;
        }

        // 'odd' index
        return index >>> 1;
    }

}
