package com.max.algs;


import org.apache.log4j.Logger;
import org.eclipse.collections.impl.set.mutable.primitive.IntHashSet;

import java.util.Objects;
import java.util.Random;
import java.util.function.IntUnaryOperator;

public final class AlgorithmsMain {

    private static final Logger LOG = Logger.getLogger(AlgorithmsMain.class);

    private AlgorithmsMain() throws Exception {

        final Random rand = new Random();

        final Class<?> clazz = org.eclipse.collections.impl.set.mutable.primitive.IntHashSet.class;

        LOG.info(clazz + " loaded");

        for (int it = 0; it < 1000; ++it) {
            int a = rand.nextInt(1_000_000_000);
            int b = rand.nextInt(1_000_000_000);
            int m = rand.nextInt(1_000_000_000);
            int x0 = rand.nextInt(1_000_000_000);

            IntUnaryOperator func = x -> (((a % m) * (x % m)) % m + (b % m)) % m;
//        IntUnaryOperator func = x -> (a * x + b) % m;

            FunctionCycle floydsCycle = findCycleWithFloyds(func, x0);

            FunctionCycle hashCycle = findCycleWithHashing(func, x0);

            FunctionCycle brentsCycle = findCycleWithBrents(func, x0);

            if (!(floydsCycle.equals(hashCycle) && floydsCycle.equals(brentsCycle) && hashCycle.equals(brentsCycle))) {
                throw new IllegalStateException("Bug detected. hashCycle:  " + hashCycle +
                        ", floydsCycle: " + floydsCycle +
                        ", brentsCycle: " + brentsCycle);
            }
        }

        LOG.info("Main done... java-" + System.getProperty("java.version"));
    }

    /**
     * Find function cycle using Brent's cycle detection algorithm.
     */
    private static FunctionCycle findCycleWithBrents(IntUnaryOperator func, int x0) {

        int power = 1;
        int lam = 1;

        int t = x0;
        int h = func.applyAsInt(x0);

        // 'lam' will be equal to cycle length at the end of iteration
        while (t != h) {
            if (power == lam) {
                t = h;
                power <<= 1;
                lam = 0;
            }

            h = func.applyAsInt(h);
            ++lam;
        }

        final int cycleLength = lam;

        t = x0;
        h = x0;

        // find star point for cycle
        for (int i = 0; i < lam; ++i) {
            h = func.applyAsInt(h);
        }

        while (t != h) {
            t = func.applyAsInt(t);
            h = func.applyAsInt(h);
        }

        return new FunctionCycle(t, cycleLength);
    }

    /**
     * Find function cycle using HashSet
     */
    private static FunctionCycle findCycleWithHashing(IntUnaryOperator func, int x0) {

        IntHashSet detectedValues = new IntHashSet();
        int curValue = x0;

        while (!detectedValues.contains(curValue)) {
            detectedValues.add(curValue);
            curValue = func.applyAsInt(curValue);
        }

        final int startPoint = curValue;

        curValue = func.applyAsInt(curValue);
        int length = 1;

        while (curValue != startPoint) {
            curValue = func.applyAsInt(curValue);
            ++length;
        }

        return new FunctionCycle(startPoint, length);
    }

    /**
     * Find function cycle using Floyd's cycle detection algorithm.
     */
    private static FunctionCycle findCycleWithFloyds(IntUnaryOperator func, int x0) {

        int t = func.applyAsInt(x0);
        int h = func.applyAsInt(t);

        // find point inside the cycle
        while (t != h) {
            t = func.applyAsInt(t);
            h = func.applyAsInt(func.applyAsInt(h));
        }

        h = x0;

        while (h != t) {
            t = func.applyAsInt(t);
            h = func.applyAsInt(h);
        }

        final int startPoint = t;
        int length = 1;
        h = func.applyAsInt(h);

        while (t != h) {
            h = func.applyAsInt(h);
            ++length;
        }

        return new FunctionCycle(startPoint, length);
    }

    private static final class FunctionCycle {
        final int startPoint;
        final int length;

        FunctionCycle(int startPoint, int length) {
            this.startPoint = startPoint;
            this.length = length;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            FunctionCycle other = (FunctionCycle) obj;

            return Objects.equals(startPoint, other.startPoint) &&
                    Objects.equals(length, other.length);
        }

        @Override
        public int hashCode() {
            return Objects.hash(startPoint, length);
        }

        @Override
        public String toString() {
            return "startPoint: " + startPoint + ", length: " + length;
        }
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
