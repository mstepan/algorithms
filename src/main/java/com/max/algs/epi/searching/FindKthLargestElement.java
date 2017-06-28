package com.max.algs.epi.searching;


import com.max.algs.util.ArrayUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 12.7. Find K-th largest element in an unsorted array.
 */
public class FindKthLargestElement {

    private static final Logger LOG = Logger.getLogger(FindKthLargestElement.class);

    private FindKthLargestElement() throws Exception {

        for (int it = 0; it < 10; ++it) {

            int[] sortedArr = ArrayUtils.generateRandomArrayOfRandomLength(1000);
            int[] arrCopy = Arrays.copyOf(sortedArr, sortedArr.length);

            Arrays.sort(sortedArr);

            for (int i = 0; i < sortedArr.length; ++i) {

                int k = arrCopy.length - i;

                if (sortedArr[i] != findKthLargest(arrCopy, k)) {
                    throw new IllegalStateException("Not equals");
                }
            }
        }

        System.out.printf("'FindKthLargestElement' completed. java-%s %n", System.getProperty("java.version"));
    }

    /**
     * time: O(N)
     * space: O(1)
     */
    public static int findKthLargest(int[] arr, int k) {
        checkNotNull(arr, "null 'arr' passed");
        checkArgument(k > 0 && k <= arr.length,
                "Incorrect 'k' value, should be in range [1, %s], but k = %s", arr.length, k);

        int from = 0;
        int to = arr.length - 1;
        int searchIndex = k;

        while (from < to) {

            assert searchIndex <= (to - from + 1) : "Incorrect boundary detected";

            int pivot = arr[to];
            int smaller = from - 1;

            for (int i = from; i < to; ++i) {
                if (arr[i] <= pivot) {
                    swapElements(arr, i, smaller + 1);
                    ++smaller;
                }
            }

            swapElements(arr, to, smaller + 1);
            ++smaller;

            int curIndex = (to - smaller) + 1;

            if (curIndex == searchIndex) {
                return arr[smaller];
            }

            // go left
            if (searchIndex > curIndex) {
                searchIndex -= curIndex;
                to = smaller - 1;
            }
            // go right
            else {
                from = smaller + 1;
            }
        }

        assert from == to : "from != to";

        return arr[from];
    }

    private static void swapElements(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        try {
            new FindKthLargestElement();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
