package com.max.algs;

import com.max.algs.util.MathUtils;

import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;


public class ConvertNumbers {

    /**
     * Given two numbers 'a' and 'b'. Convert  'a' to 'b' with minimum number of operations.
     * The only possible operations are: "+1" and "*2".
     * <p>
     * For example: convert "4" to "6"
     * 1. [4] - 1 = 3
     * 2. 3 * 2 = [6]
     * <p>
     * <p>
     * Use BFS like search.
     * <p>
     * k - shortest path
     * time: O(2^k)
     * space: O(2^k)
     */
    public static String findShortestTransformation(int value, int valueToReach) {

        int handledNodes = 0;

        Deque<Map.Entry<Integer, String>> queue = new ArrayDeque<>();

        queue.add(new AbstractMap.SimpleEntry<>(value, ""));

        while (!queue.isEmpty()) {
            Map.Entry<Integer, String> solution = queue.poll();
            ++handledNodes;

            int solutionValue = solution.getKey();
            String solutionRes = solution.getValue();

            if (solutionValue == valueToReach) {
                System.out.println("handledNodes = " + handledNodes);
                return solution.getValue();
            }

            queue.add(new AbstractMap.SimpleEntry<>(solutionValue - 1, solutionRes + " -1"));

            if (solutionValue < valueToReach) {
                queue.add(new AbstractMap.SimpleEntry<>(solutionValue * 2, solutionRes + " *2"));
            }
        }

        return "";
    }

    private static final class PartialSolution implements Comparable<PartialSolution> {

        final int searchValue;
        final int value;
        final String res;
        final int resSize;

        public PartialSolution(int searchValue, int value, String res, int resSize) {
            this.searchValue = searchValue;
            this.value = value;
            this.res = res;
            this.resSize = resSize;
        }

        /**
         * 'A*' heuristic function 'h'.
         * This heuristic is admissible and consistent (satisfy triangle inequality).
         */
        private int expectedLength() {

            if (value == searchValue) {
                return 0;
            }

            if (value > searchValue) {
                return value - searchValue;
            }

            return (int) MathUtils.log2(Math.round((double) searchValue / value));
        }

        @Override
        public int compareTo(PartialSolution other) {

            int curCost = resSize + expectedLength();
            int otherCost = other.resSize + other.expectedLength();

            return Integer.compare(curCost, otherCost);
        }

        @Override
        public String toString() {
            return String.valueOf(value) + ", res: " + res;
        }
    }

    /**
     * Find shortest path using A* method.
     */
    public static String findShortestTransformationAStar(int value, int valueToReach, boolean useClosedSet) {

        Set<Integer> closedNodes = new HashSet<>();
        int handledNodes = 0;
        Queue<PartialSolution> queue = new PriorityQueue<>();

        queue.add(new PartialSolution(valueToReach, value, "", 0));

        while (!queue.isEmpty()) {

            PartialSolution solution = queue.poll();
            if (useClosedSet) {
                closedNodes.add(solution.value);
            }
            ++handledNodes;

            if (solution.value == valueToReach) {
                if (useClosedSet) {
                    System.out.println("handledNodes(A*, closed set) = " + handledNodes);
                }
                else {
                    System.out.println("handledNodes(A*) = " + handledNodes);
                }
                return solution.res;
            }

            if (!closedNodes.contains(solution.value - 1)) {
                queue.add(new PartialSolution(valueToReach, solution.value - 1, solution.res + " -1", solution.resSize + 1));
            }

            if (solution.value < valueToReach && !closedNodes.contains(solution.value * 2)) {
                queue.add(new PartialSolution(valueToReach, solution.value * 2, solution.res + " *2", solution.resSize + 1));
            }
        }

        return "";
    }

    public static void main(String[] args) {

        final int bound = 1000;
        Random rand = new Random();

        /*
        1461 => 3452
        handledNodes = 36_000_504
        handledNodes(A*) = 180_322
        handledNodes(A*, closed set) = 2_658
         */
        int a = rand.nextInt(bound);
        int b = rand.nextInt(bound);

        String resBruteforce = ConvertNumbers.findShortestTransformation(a, b);
        String resAStar = ConvertNumbers.findShortestTransformationAStar(a, b, false);
        String resAStarOptimised = ConvertNumbers.findShortestTransformationAStar(a, b, true);

        System.out.printf("%d => %d: %s %n", a, b, resBruteforce);
        System.out.printf("%d => %d: %s %n", a, b, resAStar);
        System.out.printf("%d => %d: %s %n", a, b, resAStarOptimised);


        if (!resBruteforce.equals(resAStar)) {
            throw new IllegalStateException("A* returned incorrect results");
        }

        System.out.println("Main done...");
    }
}
