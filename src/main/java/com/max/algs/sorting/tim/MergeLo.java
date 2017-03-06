package com.max.algs.sorting.tim;

import java.util.Arrays;

/**
 * Merge left smaller sub array with right bigger one.
 */
final class MergeLo {

    private MergeLo() {
    }

    public static void merge(int[] arr, Run left, Run right, int minGallop) {

        int[] temp = Arrays.copyOfRange(arr, left.from, left.to + 1);

        int copyIndex = left.from;

        int leftIndex = 0;
        int rightIndex = right.from;

        int leftValue = temp[leftIndex];
        int rightValue = arr[rightIndex];

        Side side = Side.LEFT;
        int copiedFromSameSide = 0;

        boolean gallopingMode = false;

        while (copyIndex <= right.to) {

            if (leftValue <= rightValue) {

                // 'galloping' mode, search in left sub array
                if (gallopingMode) {

                    int lastIndex = leftIndex;

                    for (int j = 1; ; j++) {

                        int offset = (1 << j) - 1;
                        int index = leftIndex + offset;

                        if (index >= temp.length || temp[index] > rightValue) {
                            break;
                        }

                        lastIndex = index;
                    }

                    int sliceLength = lastIndex - leftIndex + 1;

                    System.arraycopy(temp, leftIndex, arr, copyIndex, sliceLength);

                    copyIndex += sliceLength;
                    leftIndex = lastIndex + 1;

                    if (sliceLength < minGallop) {
                        gallopingMode = false;
                    }

                    if (leftIndex >= temp.length) {
                        break;
                    }

                    leftValue = temp[leftIndex];
                }

                // simple mode
                else {

                    arr[copyIndex] = leftValue;
                    ++leftIndex;

                    if (leftIndex >= temp.length) {
                        break;
                    }

                    leftValue = temp[leftIndex];
                    ++copyIndex;

                    if (side == Side.LEFT) {
                        ++copiedFromSameSide;
                    }
                    else {
                        side = Side.LEFT;
                        copiedFromSameSide = 1;
                    }

                    if (copiedFromSameSide == minGallop) {
                        gallopingMode = true;
                    }
                }

            }
            else {

                // 'galloping' mode, search in right sub array
                if (gallopingMode) {

                    int lastIndex = rightIndex;

                    for (int j = 1; ; j++) {

                        int offset = (1 << j) - 1;
                        int index = rightIndex + offset;

                        if (index > right.to || arr[index] > leftValue) {
                            break;
                        }

                        lastIndex = index;
                    }

                    int sliceLength = lastIndex - rightIndex + 1;

                    System.arraycopy(arr, rightIndex, arr, copyIndex, sliceLength);

                    copyIndex += sliceLength;
                    rightIndex = lastIndex + 1;

                    rightValue = (rightIndex <= right.to ? arr[rightIndex] : Integer.MAX_VALUE);

                    if (sliceLength < minGallop) {
                        gallopingMode = false;
                    }
                }

                // simple mode
                else {
                    arr[copyIndex] = rightValue;
                    ++rightIndex;
                    rightValue = (rightIndex <= right.to ? arr[rightIndex] : Integer.MAX_VALUE);
                    ++copyIndex;

                    if (side == Side.RIGHT) {
                        ++copiedFromSameSide;
                    }
                    else {
                        side = Side.RIGHT;
                        copiedFromSameSide = 1;
                    }

                    if (copiedFromSameSide == minGallop) {
                        gallopingMode = true;
                    }
                }
            }
        }

        Run mergedRun = new Run(arr, left.from, right.to);
        assert mergedRun.isSorted() : "just merged run isn't sorted: " + mergedRun;
    }
}
