package com.max.algs.epi.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class OnlineSampling {

    /**
     * time: O(N)
     * space: O(K)
     */
    public static int[] onlineSampling(Iterator<Integer> it, int k) {

        int[] sample = new int[k];

        for (int i = 0; i < k && it.hasNext(); ++i) {
            sample[i] = it.next();
        }

        Random rand = new Random();

        // elements handled so far
        int elemsCount = k + 1;

        while (it.hasNext()) {

            int value = it.next();

            int randIndex = rand.nextInt(elemsCount);

            /*
            Add element to any 'k' bucket with probability = k/elemsCount
             */
            if (randIndex < k) {
                sample[randIndex] = value;
            }

            ++elemsCount;
        }

        return sample;
    }

    private OnlineSampling() throws Exception {

        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < 100; ++i) {
            data.add(i);
        }

        int sampleSize = 5;
        int[] sample = onlineSampling(data.iterator(), sampleSize);

        System.out.println(Arrays.toString(sample));

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new OnlineSampling();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
