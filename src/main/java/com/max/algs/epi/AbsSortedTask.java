package com.max.algs.epi;

public class AbsSortedTask {

    public static void main(String[] args) {
        int[] array = new int[]{-49, 75, 103, -147, 164, -197, -238, 314, 348, -422};
        int p = -385;

        int minIndex = min(array);
        int maxIndex = max(array);

        while (minIndex != -1 || maxIndex != -1) {
            if (maxIndex == minIndex) {
                System.out.println(String.format("indexes: %d, %d", -1, -1));
                return;
            }
            if (array[minIndex] + array[maxIndex] == p) {
                System.out.println(String.format("indexes: %d, %d", minIndex, maxIndex));
                return;
            }

            if (array[minIndex] + array[maxIndex] < p) {
                minIndex = next(array, minIndex);
            }
            else if (array[minIndex] + array[maxIndex] > p) {
                maxIndex = prev(array, maxIndex);
            }
        }

        System.out.println(String.format("indexes: %d, %d", -1, -1));

    }

    private static int min(int[] array) {
        if (array.length == 1) {
            return 0;
        }

        int min = Integer.MAX_VALUE;
        int minIndex = array.length - 1;
        for (int index = array.length - 1; index >= 0; index--) {
            if (array[index] < 0) {
                return index;
            }

            if (min > array[index]) {
                min = array[index];
                minIndex = index;
            }
        }

        return minIndex;
    }

    private static int max(int[] array) {
        if (array.length == 1) {
            return 0;
        }

        int max = Integer.MIN_VALUE;
        int maxIndex = array.length - 1;
        for (int index = array.length - 1; index >= 0; index--) {
            if (array[index] > 0) {
                return index;
            }

            if (max < array[index]) {
                max = array[index];
                maxIndex = index;
            }
        }

        return maxIndex;
    }

    private static int next(int[] array, int index) {
        int element = array[index];
        int nextIndex = index + 1;

        if (element < 0) {
            for (nextIndex = index - 1; nextIndex >= 0; nextIndex--) {
                if (array[nextIndex] < 0) {
                    return nextIndex;
                }
            }
            nextIndex = 0;
        }

        for (; nextIndex < array.length; nextIndex++) {
            if (array[nextIndex] >= 0) {
                return nextIndex;
            }
        }

        return -1;
    }

    private static int prev(int[] array, int index) {
        int element = array[index];
        int nextIndex = index > 0 ? index - 1 : 0;

        if (element >= 0) {
            for (nextIndex = index - 1; nextIndex >= 0; nextIndex--) {
                if (array[nextIndex] >= 0) {
                    return nextIndex;
                }
            }
            nextIndex = 0;
        }

        for (; nextIndex < array.length; nextIndex++) {
            if (array[nextIndex] < 0) {
                return nextIndex;
            }
        }

        return -1;
    }
}
