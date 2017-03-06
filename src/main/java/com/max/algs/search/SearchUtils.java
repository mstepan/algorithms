package com.max.algs.search;

public final class SearchUtils {

    private SearchUtils() {
        super();
    }

    /**
     * time: O(N) space: O(1)
     * <p>
     * Comparisons count: 3*n/2 - 2
     */
    public static int[] findMinMax(int[] arr) {

        if (arr == null) {
            throw new IllegalArgumentException(
                    "Can't find min and max in NULL array");
        }

        if (arr.length == 0) {
            throw new IllegalArgumentException(
                    "Can't find min and max in EMPTY array");
        }

        if (arr.length == 1) {
            return new int[]{arr[0], arr[0]};
        }

        int min = arr[0];
        int max = arr[1];

        if (min > max) {
            min = arr[1];
            max = arr[0];
        }

        int curMin;
        int curMax;

        for (int i = 3; i < arr.length; i += 2) {

            curMin = arr[i - 1];
            curMax = arr[i];

            if (curMin > curMax) {
                curMin = arr[i];
                curMax = arr[i - 1];
            }

            if (curMin < min) {
                min = curMin;
            }

            if (curMax > max) {
                max = curMax;
            }
        }

        int length = arr.length;

        // odd length
        if ((length & 1) == 1) {
            int last = arr[length - 1];

            if (last < min) {
                min = last;
            }
            else if (last > max) {
                max = last;
            }
        }

        return new int[]{min, max};
    }

    public static int linearSearch(int[] arr, int value) {
        checkForNull(arr);

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                return i;
            }
        }

        return -1;
    }

    /*
     * Consider an array of integers wherein each element is +1 or -1 its
     * preceding element. Given a number, find the first occurence of this
     * number (index) in this array without using linear search.
     *
     * For example, consider the array : 4 5 6 5 6 7 8 9 10 9 10 (each element
     * in this array is +1 or -1 its preceding element)
     *
     * Input : 10 (find first occurence of 10 without using linear search)
     * Output : 8
     */
    public static int specialSearch(int[] arr, int value) {

        checkForNull(arr);

        int index = 0;
        int offset = 0;

        while (index < arr.length) {

            if (arr[index] == value) {
                return index;
            }
            offset = Math.abs(arr[index] - value);
            index += offset;
        }

        return -1;
    }

    public static int bsearch(int[] arr, int searchValue) {

        int from = 0;
        int to = arr.length - 1;
        int mid;

        while (from != to) {

            mid = (from + to) >>> 1;

            if (arr[mid] < searchValue) {
                from = mid + 1;
            }
            else {
                to = mid;
            }
        }

        if (arr[from] == searchValue) {
            return from;
        }

        return -1;
    }

    /**
     * Very quick binary search implementation.
     */
    public static int binarySearch(int[] arr, int value) {

        int length = arr.length;
        int half = length >> 1;

        int lower = 0;
        int middle;

        while (half != 0) {
            middle = lower + half;
            lower = arr[middle] <= value ? middle : lower;

            length -= half;
            half = length >> 1;
        }

        return arr[lower] == value ? lower : -1;
    }

    /**
     * time: O(lglgN) space: O(1)
     */
    public static int interpolationSearch(int[] arr, int key) {

        int from = 0;
        int to = arr.length - 1;

        int mid;
        double coeff;

        while (from <= to) {

            if (arr[to] - arr[from] <= 0 || key - arr[from] <= 0) {
                coeff = 0.5;
            }
            else {
                coeff = ((double) key - arr[from]) / (arr[to] - arr[from]);
            }
            mid = (int) (from + (coeff * (to - from)));

            if (arr[mid] == key) {
                return mid;
            }

            if (arr[mid] > key) {
                to = mid - 1;
            }
            else {
                from = mid + 1;
            }
        }

        return -1;

    }

    /**
     * time: O(lgN) space: O(1)
     */
    public static int bsearchClassic(int[] arr, int key) {

        int from = 0;
        int to = arr.length - 1;

        int mid;

        while (from <= to) {

            mid = (from + to) >>> 1;

            if (arr[mid] == key) {
                return mid;
            }

            if (arr[mid] > key) {
                to = mid - 1;
            }
            else {
                from = mid + 1;
            }
        }

        return -1;
    }

    private static void checkForNull(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("NULL array passed as parameter");
        }
    }

}
