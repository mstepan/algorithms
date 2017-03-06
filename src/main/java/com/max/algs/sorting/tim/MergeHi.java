package com.max.algs.sorting.tim;

import java.util.Arrays;

/**
 * Merge left bigger sub array with right smaller from backward.
 */
final class MergeHi {

    private MergeHi() {
    }

    public static void merge(int[] arr, Run left, Run right, int minGallop) {

        //TODO: add 'galloping' mode here

        int[] temp = Arrays.copyOfRange(arr, right.from, right.to + 1);

        int copyIndex = right.to;
        int leftIndex = left.to;
        int rightIndex = temp.length - 1;

        int leftValue = arr[leftIndex];
        int rightValue = temp[rightIndex];

        while (copyIndex >= left.from) {

            if (leftValue > rightValue) {
                arr[copyIndex] = leftValue;
                --leftIndex;
                leftValue = (leftIndex >= left.from ? arr[leftIndex] : Integer.MIN_VALUE);
            }
            else {
                arr[copyIndex] = rightValue;
                --rightIndex;

                if (rightIndex < 0) {
                    break;
                }

                rightValue = temp[rightIndex];
            }

            --copyIndex;
        }
    }
}
