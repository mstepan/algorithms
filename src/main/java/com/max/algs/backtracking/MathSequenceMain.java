package com.max.algs.backtracking;

import com.max.algs.util.MathUtils;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Problem statement:
 * Can one start at 4 and reach any integer by iterating factorial, sqrt, and floor?
 * <p>
 * http://mathforum.org/wagon/current_solutions/s1171.html
 */
public class MathSequenceMain {

    private static final double INITIAL_VALUE = 4.0;
    private static final double UPPER_THRESHOLD = Long.MAX_VALUE;


    private MathSequenceMain() throws Exception {
        findSolution(26);

        System.out.println("Main done...");
    }

    public static void main(String[] args) {
        try {
            new MathSequenceMain();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void findSolution(int value) {
        findSolutionRec(value, new ArrayDeque<>(), INITIAL_VALUE);
    }

    private void findSolutionRec(int searchValue, Deque<String> operations, double curValue) {

        if (Double.compare(curValue, searchValue) == 0) {
            System.out.println("Solution for " + searchValue + ": " + operations);
            return;
        }

        if (Double.compare(curValue, INITIAL_VALUE) < 0 || Double.compare(curValue, UPPER_THRESHOLD) > 0) {
            return;
        }

        // factorial
        if (isIntegerValue(curValue)) {
            operations.addLast("!");
            findSolutionRec(searchValue, operations, MathUtils.factorialBig(BigInteger.valueOf((long) curValue)).doubleValue());
            operations.pollLast();
        }

        // square root
        operations.addLast("sqrt");
        findSolutionRec(searchValue, operations, Math.sqrt(curValue));
        operations.pollLast();

        // floor
        if (!isIntegerValue(curValue)) {
            operations.addLast("floor");
            findSolutionRec(searchValue, operations, Math.floor(searchValue));
            operations.pollLast();
        }
    }

    private boolean isIntegerValue(double value) {
        return Math.floor(value) == Math.ceil(value);
    }
}
