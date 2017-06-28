package com.max.algs.epi.heaps;


import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Print online median.
 */
public class OnlineMedian {

    private static final Logger LOG = Logger.getLogger(OnlineMedian.class);


    private OnlineMedian() throws Exception {

        int[] arr = {0, 3, 0, 9, 4, 6, 5, 3, 3, 9}; //ArrayUtils.generateRandomArray(10, 10);

        System.out.println(Arrays.toString(arr));

        printOnlineMedian(arr);

        System.out.printf("'OnlineMedian' completed. java-%s %n", System.getProperty("java.version"));
    }

    /**
     * time: O(N*lgN)
     * space: O(N)
     */
    private static void printOnlineMedian(int[] arr) {
        checkNotNull(arr);

        if (arr.length == 0) {
            return;
        }

        // left side
        Queue<Integer> maxHeap = new PriorityQueue<>(arr.length >> 1, Collections.reverseOrder());

        // right side
        Queue<Integer> minHeap = new PriorityQueue<>(arr.length >> 1);

        for (int val : arr) {

            addValueToHeap(val, maxHeap, minHeap);

            rebalanceHeaps(maxHeap, minHeap);

            System.out.println(calculateMedianFromHeaps(maxHeap, minHeap));
        }
    }

    private static void addValueToHeap(int val, Queue<Integer> maxHeap, Queue<Integer> minHeap) {
        if (maxHeap.isEmpty() || val <= maxHeap.peek()) {
            maxHeap.add(val);
        }
        else {
            minHeap.add(val);
        }
    }

    private static void rebalanceHeaps(Queue<Integer> maxHeap, Queue<Integer> minHeap) {
        if (minHeap.size() > maxHeap.size()) {
            maxHeap.add(minHeap.poll());
        }
        else if (maxHeap.size() - minHeap.size() == 2) {
            minHeap.add(maxHeap.poll());
        }

        assert Math.abs(maxHeap.size() - minHeap.size()) <= 1 : "Unbalanced heaps detected";
        assert maxHeap.size() <= minHeap.size() : "Incorrect maxHeap (left side) detected";
    }

    private static double calculateMedianFromHeaps(Queue<Integer> maxHeap, Queue<Integer> minHeap) {
        if (minHeap.size() == maxHeap.size()) {
            return ((double) maxHeap.peek() + minHeap.peek()) / 2.0;
        }
        return maxHeap.peek();
    }

    public static void main(String[] args) {
        try {
            new OnlineMedian();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
