package com.max.algs.epi.string;

import org.apache.log4j.Logger;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;


public class ReplaceAndRemove {

    private static final Logger LOG = Logger.getLogger(ReplaceAndRemove.class);

    private ReplaceAndRemove() throws Exception {

        char[] arr = "acaa   ".toCharArray();

        System.out.println(Arrays.toString(arr));

        int length = transform(arr, 4);

        char[] transformedArr = Arrays.copyOf(arr, length);

        System.out.println(Arrays.toString(transformedArr));

        System.out.println("Variant:");
        int[] left = {1, 8, 10, 18, 19, 0, 0, 0, 0};
        int[] right = {4, 7, 12, 22};

        System.out.println(Arrays.toString(left));
        System.out.println(Arrays.toString(right));

        merge(left, 5, right);

        System.out.println(Arrays.toString(left));
        System.out.println(Arrays.toString(right));

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * 7.4. Replace and remove.
     * <p>
     * a -> dd
     * b -> removed
     * <p>
     * time: O(N)
     * space: O(1)
     */
    public static int transform(char[] arr, int length) {
        checkArgument(arr != null, "null 'arr' passed as argument");
        checkArgument(length >= 0 && length <= arr.length);

        final int newLength = calculateNewLength(arr, length);

        checkArgument(newLength <= arr.length, "Check failed 'newLength <= arr.length': newLength = %s, arr.length = %s",
                newLength, arr.length);

        int index = removeB(arr, length);

        replaceA(arr, index, newLength);

        return newLength;
    }

    private static int calculateNewLength(char[] arr, int length) {

        int newLength = length;

        for (int i = 0; i < length; ++i) {
            if (arr[i] == 'b') {
                --newLength;
            }
            else if (arr[i] == 'a') {
                ++newLength;
            }
        }
        return newLength;
    }

    private static int removeB(char[] arr, int length) {

        int index = 0;

        // remove 'b'
        for (int i = 0; i < length; ++i) {
            if (arr[i] != 'b') {
                arr[index] = arr[i];
                ++index;
            }
        }

        return index;
    }

    private static void replaceA(char[] arr, int index, int newLength) {
        int pos = newLength - 1;

        // replace 'a' -> 'dd'
        for (int i = index - 1; i >= 0; --i) {
            if (arr[i] == 'a') {
                arr[pos] = 'd';
                arr[pos - 1] = 'd';
                pos -= 2;
            }
            else {
                arr[pos] = arr[i];
                --pos;
            }
        }
    }

    /**
     * Merge 2 sorted arrays. First array have enough free space to do in-place merge.
     * <p>
     * time: O(N)
     * space: O(1)
     */
    public static void merge(int[] leftArr, int leftLength, int[] rightArr) {

        int leftI = leftLength - 1;
        int rightI = rightArr.length - 1;

        for (int index = leftArr.length - 1; index >= 0; --index) {

            int leftValue = leftI >= 0 ? leftArr[leftI] : Integer.MIN_VALUE;
            int rightValue = rightI >= 0 ? rightArr[rightI] : Integer.MIN_VALUE;

            if (leftValue >= rightValue) {
                leftArr[index] = leftValue;
                --leftI;
            }
            else {
                leftArr[index] = rightValue;
                --rightI;
            }
        }

    }

    public static void main(String[] args) {
        try {
            new ReplaceAndRemove();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
