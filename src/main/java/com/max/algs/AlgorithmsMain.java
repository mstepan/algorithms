package com.max.algs;


import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

public final class AlgorithmsMain {

    private static final Logger LOG = Logger.getLogger(AlgorithmsMain.class);


    private AlgorithmsMain() throws Exception {

        int[] scores = {100, 100, 50, 40, 40, 20, 10};
        int[] alice = {5, 25, 50, 120};

        Thread th1 = new Thread(() -> {
            int[] res = climbingLeaderboard(scores, alice);
            System.out.println(Arrays.toString(res));
        });

        Thread th2 = new Thread(() -> {
            sleep(1L);
            scores[0] = -100;
            alice[0] = 155;
        });

        th1.start();
        th2.start();

        th1.join();
        th2.join();

        LOG.info("Main completed.");
    }

    private static void sleep(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread interrupted", ex);
        }
    }

    /**
     * N = scores.length
     * M = alice.length
     * <p>
     * time: O(M*lgN)
     * space:O(M+N)
     */
    static int[] climbingLeaderboard(int[] scoresOriginal, int[] aliceOriginal) {

        checkArgument(scoresOriginal != null, "null scores passed");
        checkArgument(aliceOriginal != null, "null alice passed");

        final int[] scores = scoresOriginal; //Arrays.copyOf(scoresOriginal, scoresOriginal.length);
        final int[] alice = aliceOriginal; //Arrays.copyOf(aliceOriginal, aliceOriginal.length);

        sleep(2);

        int[] ranks = new int[scores.length];
        ranks[0] = 1;

        for (int i = 1; i < ranks.length; ++i) {
            checkState(scores[i] <= scores[i - 1], "Not a decreased array detected");
            ranks[i] = (scores[i] == scores[i - 1] ? ranks[i - 1] : ranks[i - 1] + 1);
        }

        int[] aliceRanks = new int[alice.length];

        for (int i = 0; i < alice.length; ++i) {
            int curScore = alice[i];

            int index = find(scores, curScore);

            // curScore is the biggest element so far
            if (index == -1) {
                aliceRanks[i] = 1;
            }
            // curScore is the smallest
            else if (index == scores.length) {
                aliceRanks[i] = ranks[ranks.length - 1] + 1;
            }
            // we found curScore in array of scores or
            // we found the first bigger element index
            else {
                aliceRanks[i] = curScore == scores[index] ? ranks[index] : ranks[index] + 1;
            }
        }

        return aliceRanks;
    }

    private static int find(int[] arr, int searchValue) {

        if (searchValue > arr[0]) {
            return -1;
        }

        if (searchValue < arr[arr.length - 1]) {
            return arr.length;
        }

        int from = 0;
        int to = arr.length - 1;

        int lastIndex = 0;

        while (from <= to) {
            int mid = from + (to - from) / 2;

            if (arr[mid] == searchValue) {
                return mid;
            }

            if (arr[mid] > searchValue) {
                lastIndex = mid;
                from = mid + 1;
            }
            else {
                to = mid - 1;
            }
        }

        checkState(lastIndex >= 0 && lastIndex < arr.length);

        return lastIndex;
    }

    public static void main(String[] args) {
        try {
            new AlgorithmsMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
