package com.max.algs.sorting;

import java.util.Deque;

public final class DequeSorter {

    private DequeSorter() {
        super();
    }

    /**
     * In place sort deque, usign only dequee specific operations (poll, add).
     * time: O(N^2)
     * space: O(1)
     */
    public static <T extends Comparable<T>> void sortDeque(Deque<T> deque) {

        int elemsLeft = deque.size() - 1;

        while (elemsLeft > 0) {

            T max = deque.poll();

            for (int count = 0; count < elemsLeft; count++) {
                T cur = deque.poll();

                if (cur.compareTo(max) < 0) {
                    deque.add(cur);
                }
                else {
                    deque.add(max);
                    max = cur;
                }
            }

            int sortedCount = deque.size() - elemsLeft;
            deque.add(max);

            while (sortedCount > 0) {
                deque.add(deque.poll());
                --sortedCount;
            }

            --elemsLeft;
        }
    }

}
