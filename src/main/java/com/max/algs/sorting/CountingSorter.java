package com.max.algs.sorting;

import com.max.algs.util.ArrayUtils;


/**
 * In-place counting sort.
 * time: O(N)
 * space: O(max_value)
 */
public final class CountingSorter {


    private CountingSorter() {
        super();
    }


    public static void sort(int[] arr) {

        int max = ArrayUtils.maxValue(arr);

        int[] countArr = new int[max + 1];

        for (int value : arr) {
            countArr[value] += 1;
        }

        int baseIndex = 0;
        int count;

        for (int i = 0; i < countArr.length; i++) {

            count = countArr[i];

            while (count > 0) {
                arr[baseIndex] = i;
                ++baseIndex;
                --count;
            }
        }
    }

}
