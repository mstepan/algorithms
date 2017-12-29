package com.max.algs.creative;

import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.ArrayDeque;
import java.util.Deque;

import static com.google.common.base.Preconditions.checkNotNull;

final class KthSmallerOrEquals {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 6.34. Find if K-th biggest element from max-heap is smaller or equals to 'x'.
     * <p>
     * time: O(K)
     * space: O(K)
     */
    private static boolean isKthSmallerOrEquals(int[] heap, int k, int x) {
        checkNotNull(heap);

        Deque<Integer> queue = new ArrayDeque<>();

        if (heap[0] < x) {
            return true;
        }

        queue.add(0);
        int order = 0;

        while (!queue.isEmpty() && order < k) {

            int parent = queue.poll();

            int left = 2 * parent + 1;
            int right = 2 * parent + 2;

            if (left < heap.length && heap[left] > x) {
                queue.add(left);
                ++order;
            }

            if (right < heap.length && heap[right] > x) {
                queue.add(right);
                ++order;
            }
        }

        return order < k;
    }

    private KthSmallerOrEquals() {

        int[] heap = {20, 10, 15, 6, 2, 12, 14, 1, 2};

        int k = 4;
        int x = 10;

        boolean res = isKthSmallerOrEquals(heap, k, x);
        LOG.info(res);

        LOG.info("KthSmallerOrEquals done...");
    }


    public static void main(String[] args) {
        try {
            new KthSmallerOrEquals();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
