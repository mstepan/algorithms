package com.max.algs.fulltextsearch;

import com.max.algs.ds.Pair;
import com.max.algs.ds.heap.BinaryHeap;
import org.apache.log4j.Logger;

import java.util.*;

public class InMemoryIndex {

    private static final Logger LOG = Logger.getLogger(InMemoryIndex.class);

    private final Map<String, List<String>> terms = new HashMap<>();

    public void add(String word, String documentId) {
        List<String> documents = terms.compute(word, (k, v) -> v == null ? new ArrayList<>() : v);
        documents.add(documentId);
    }

    void printStatistics() {

        int pointersCount = 0;
        int size = 100;
        BinaryHeap<FreqPair> minHeap = BinaryHeap.minHeap(size + 1);

        for (Map.Entry<String, List<String>> entry : terms.entrySet()) {

            pointersCount += entry.getValue().size();

            if (minHeap.size() > size) {
                minHeap.poll();
            }

            minHeap.add(new FreqPair(entry.getKey(), entry.getValue().size()));
        }

        minHeap.poll();

        while (!minHeap.isEmpty()) {
            FreqPair pair = minHeap.poll();
            LOG.info("Most frequent word is '" + pair.getFirst() + "', with frequency = " + pair.getSecond() + ", " +
                    pair.getFirst().length());
        }

        LOG.info("Total words count: " + terms.size() + ", files pointers count: " + pointersCount);
    }

    static class FreqPair extends Pair<String, Integer> implements Comparable<FreqPair> {

        private static final long serialVersionUID = -8673707049695877808L;

        FreqPair(String word, Integer frequency) {
            super(word, frequency);
        }

        @Override
        public int compareTo(FreqPair other) {
            return Integer.compare(second, other.second);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(second);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            FreqPair other = (FreqPair) obj;
            return Objects.equals(second, other.second);
        }

    }

}
