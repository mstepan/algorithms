package com.max.algs.ds.probabalistic;

import com.max.algs.hashing.universal.UniversalHashFunction;
import com.max.algs.util.MathUtils;

/**
 * Count-min sketch frequency calculation data structure.
 * <p>
 * Example:
 * <p>
 * error rate = 0.001 (aka 0.1%)
 * accuracy = 0.999 (aka 99.9%)
 * <p>
 * Calculated parameters: width = 2719, depth = 7
 * <p>
 * Space used = (2719 * 7 * 4 bytes) = 74KB
 */
public class CountMinSketch {

    // ε (Epsilon) is 'how much error is added to our counts with each item we add to the sketch', accuracy (1%)
    private static final double DEFAULT_EPSILON = 0.01;

    // δ (Delta) is 'with what probability do we want to allow the count estimate to be outside of
    // our DEFAULT_EPSILON error rate', 1-δ = 0.99 (99% that we are within our accuracy)
    private static final double DEFAULT_DELTA = 0.01;

    private final int depth;
    private final int width;

    private final int[][] counters;
    private final UniversalHashFunction<String>[] hashFunctions;

    public CountMinSketch(double errorRate, double accuracy) {
        this(errorRate, 1.0 - accuracy, 0);
    }

    public CountMinSketch() {
        this(DEFAULT_EPSILON, DEFAULT_DELTA, 0);
    }

    @SuppressWarnings("unchecked")
    private CountMinSketch(double epsilon, double delta, int dummy) {

        width = calculateWidth(epsilon);
        depth = calculateDepth(delta);

        System.out.println("width = " + width);
        System.out.println("depth = " + depth);

        counters = new int[depth][width];

        hashFunctions = new UniversalHashFunction[counters.length];

        for (int i = 0; i < hashFunctions.length; ++i) {
            hashFunctions[i] = UniversalHashFunction.generate();
        }
    }

    private int calculateDepth(double deltaValue) {
        return (int) Math.ceil(MathUtils.ln(1.0 / deltaValue));
    }

    private int calculateWidth(double epsilonValue) {
        return (int) Math.ceil(Math.E / epsilonValue);
    }

    public void add(String value) {
        applyLogicToCounters(value, new IncCounters());
    }

    public int getCount(String value) {

        FindMinCounter findMinCounter = new FindMinCounter();

        applyLogicToCounters(value, findMinCounter);

        return findMinCounter.cnt == Integer.MAX_VALUE ? 0 : findMinCounter.cnt;
    }

    private void applyLogicToCounters(String value, CountersVisitor handler) {

        for (int i = 0; i < hashFunctions.length; ++i) {
            UniversalHashFunction<String> func = hashFunctions[i];

            int hashValue = (func.hash(value) & 0x7F_FF_FF_FF) % width;

            handler.handle(i, hashValue);
        }
    }

    @FunctionalInterface
    private interface CountersVisitor {
        void handle(int row, int col);
    }

    private class IncCounters implements CountersVisitor {
        @Override
        public void handle(int row, int col) {
            ++CountMinSketch.this.counters[row][col];
        }
    }

    private class FindMinCounter implements CountersVisitor {

        private int cnt = Integer.MAX_VALUE;

        @Override
        public void handle(int row, int col) {
            cnt = Math.min(CountMinSketch.this.counters[row][col], cnt);
        }
    }

}
