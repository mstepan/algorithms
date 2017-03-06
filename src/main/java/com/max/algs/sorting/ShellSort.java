package com.max.algs.sorting;

import static com.google.common.base.Preconditions.checkNotNull;


public class ShellSort {


    /**
     * Use precomputed sequence: h = 3*h + 1
     */
    private static final int[] H_STEPS = {
            1, 4, 13, 40, 121, 364, 1093, 3280, 9841, 29524, 88573, 265720, 797161, 2391484, 7174453,
            21523360, 64570081, 193710244, 581130733, 1743392200
    };

    /**
     * Use sequence: h = 3*h + 1
     */
    public static void sort(int[] arr) {

        checkNotNull(arr);

        final int length = arr.length;

        int h = 1;
        int thirdOfLength = length / 3;

        for (int val : H_STEPS) {
            if (val > thirdOfLength) {
                h = val;
                break;
            }
        }

        int temp;
        int j;

        while (h >= 1) {

            for (int i = h; i < length; ++i) {

                temp = arr[i];
                j = i - h;

                while (j >= 0 && arr[j] > temp) {
                    arr[j + h] = arr[j];
                    j -= h;
                }

                arr[j + h] = temp;
            }

            h /= 3;
        }

    }

}
