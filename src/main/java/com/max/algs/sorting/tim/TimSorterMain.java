package com.max.algs.sorting.tim;

import com.max.algs.util.ArrayUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Random;


public final class TimSorterMain {

    private static final Logger LOG = Logger.getLogger(TimSorterMain.class);

    private TimSorterMain() throws Exception {

        Random rand = new Random();

        for (int k = 0; k < 1000; k++) {

            int[] arr = ArrayUtils.generateRandomArray(100 + rand.nextInt(10_000), 10_000);

//            int[] arr = ArrayUtils.generateRandomArray(10, 10);

//            System.out.println(Arrays.toString(arr));

            int[] arrCopy = Arrays.copyOf(arr, arr.length);

            TimSorter sort = new TimSorter();
            sort.sort(arr);

//            System.out.println(Arrays.toString(arr));

            Arrays.sort(arrCopy);

            if (!Arrays.equals(arr, arrCopy)) {
                throw new IllegalStateException("Array wasn't sorted");
            }
        }

        System.out.println("All arrays are sorted");

        System.out.println("TimSorterMain completed...");
    }

    public static void main(String[] args) {
        try {
            new TimSorterMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) {
                System.out.println("out of order: " + arr[i - 1] + " and " + arr[i]);
                return false;
            }
        }
        return true;
    }
}
