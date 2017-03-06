package com.max.algs.concurrency;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Thread safe.
 */
public class MostVisitedPages {

    private static final int MIN_PAGE_LENGTH = 3;

    private final ConcurrentMap<String, LongAdder> pages = new ConcurrentHashMap<>();

    /**
     * time: O(1)
     */
    public void add(String page) {

        checkArgument(isValidPage(page), "Can't store page '" + page + "'");

        pages.computeIfAbsent(page, p -> new LongAdder()).increment();
    }


    /**
     * time: O(N*lgK)
     * space: O(K)
     */
    public List<String> common(int count) {

        checkArgument(count > 0, "'count' can't be negative or 0: count = " + count);

        int elems = Math.min(count, pages.size());

        PriorityQueue<Map.Entry<String, LongAdder>> minHeap = new PriorityQueue<>(elems,
                new Comparator<Map.Entry<String, LongAdder>>() {
                    @Override
                    public int compare(Map.Entry<String, LongAdder> entry1, Map.Entry<String, LongAdder> entry2) {
                        return Long.compare(entry1.getValue().longValue(), entry2.getValue().longValue());
                    }
                });

        for (Map.Entry<String, LongAdder> entry : pages.entrySet()) {
            if (minHeap.size() < elems) {
                minHeap.add(entry);
            }
            else {
                Map.Entry<String, LongAdder> minEntry = minHeap.peek();

                if (minEntry.getValue().longValue() < entry.getValue().longValue()) {
                    minHeap.poll();
                    minHeap.add(entry);
                }
            }
        }

        return minHeap.stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    private boolean isValidPage(String page) {
        if (page == null) {
            return false;
        }

        String trimmedPage = page.trim();

        return trimmedPage.length() > MIN_PAGE_LENGTH;
    }

}
