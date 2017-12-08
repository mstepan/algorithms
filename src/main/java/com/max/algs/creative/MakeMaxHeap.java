package com.max.algs.creative;

import com.max.algs.util.ArrayUtils;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;

final class MakeMaxHeap {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Make max-heap using top-down approach.
     * time: O(N*lgN)
     * space: O(1)
     */
    private static void makeHeapFromTop(int[] arr) {

        for (int i = 1; i < arr.length; ++i) {

            int cur = i;
            int parent = parentIndex(cur);

            while (cur != 0 && arr[parent] < arr[cur]) {

                swap(arr, parent, cur);

                cur = parent;
                parent = parentIndex(cur);
            }
        }
    }

    /**
     * Make max-heap using bottom-up approach.
     * time: O(N)
     * space: O(1)
     */
    private static void makeHeapFromDown(int[] arr) {

        final int childBoundary = arr.length / 2;

        for (int i = childBoundary - 1; i >= 0; --i) {

            int cur = i;

            while (cur < childBoundary) {

                int leftChild = 2 * cur + 1;
                int rightChild = 2 * cur + 2;

                int biggerIndex = cur;

                if (leftChild < arr.length && arr[leftChild] > arr[biggerIndex]) {
                    biggerIndex = leftChild;
                }

                if (rightChild < arr.length && arr[rightChild] > arr[biggerIndex]) {
                    biggerIndex = rightChild;
                }

                if (biggerIndex == cur) {
                    break;
                }

                swap(arr, cur, biggerIndex);

                cur = biggerIndex;
            }
        }
    }

    private static int parentIndex(int childIndex) {
        return ((int) Math.ceil(childIndex / 2.0)) - 1;
    }

    /**
     * time: O(N)
     * space: O(1)
     */
    private static boolean isMaxHeap(int[] arr) {

        assert arr != null;

        for (int i = 1; i < arr.length; ++i) {
            int parent = parentIndex(i);

            if (arr[parent] < arr[i]) {
                return false;
            }

        }

        return true;
    }


    private static void swap(int[] arr, int from, int to) {
        int temp = arr[from];
        arr[from] = arr[to];
        arr[to] = temp;
    }

    private MakeMaxHeap() {

        for (int it = 0; it < 100; ++it) {
            int[] arr1 = ArrayUtils.generateRandomArray(1000, 1000);
            int[] arr2 = Arrays.copyOf(arr1, arr1.length);

            makeHeapFromTop(arr1);

            if (!isMaxHeap(arr1)) {
                LOG.info("Top approach is not working");
            }

            makeHeapFromDown(arr2);

            if (!isMaxHeap(arr2)) {
                LOG.info("Down approach is not working");
            }
        }


        LOG.info("MakeMaxHeap done...");
    }


    public static void main(String[] args) {
        try {
            new MakeMaxHeap();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
