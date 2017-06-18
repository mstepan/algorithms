package com.max.algs.hanoi;

import org.apache.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;


public final class HanoiiSimulator {


    private static final Logger LOG = Logger.getLogger(HanoiiSimulator.class);


    private HanoiiSimulator() {
        super();
    }

    /**
     * N - disks count
     * <p>
     * time: O(2^n)
     * space: O(1)
     */
    public static void solve(int disksCount) {

        Deque<Integer> a = new ArrayDeque<>(disksCount);

        for (int i = disksCount; i > 0; i--) {
            a.push(i);
        }

        Deque<Integer> b = new ArrayDeque<>(disksCount);
        Deque<Integer> c = new ArrayDeque<>(disksCount);

        while (c.size() != disksCount) {

            // even disks count
            if ((disksCount & 1) == 0) {
                makeEvenMoves(a, b, c, disksCount);
            }
            // odd disks count
            else {
                makeOddMoves(a, b, c, disksCount);
            }

        }
    }

    /**
     * For an EVEN number of disks:
     * make the legal move between pegs A and B
     * make the legal move between pegs A and C
     * make the legal move between pegs B and C
     * repeat until complete
     */
    private static void makeEvenMoves(Deque<Integer> a, Deque<Integer> b, Deque<Integer> c, int disksCount) {
        while (c.size() != disksCount) {
            makeMoves(a, b, "a", "b");
            makeMoves(a, c, "a", "c");
            makeMoves(b, c, "b", "c");
        }
    }

    /**
     * For an ODD number of disks:
     * make the legal move between pegs A and C
     * make the legal move between pegs A and B
     * make the legal move between pegs C and B
     * repeat until complete
     */
    private static void makeOddMoves(Deque<Integer> a, Deque<Integer> b, Deque<Integer> c, int disksCount) {
        while (true) {
            makeMoves(a, c, "a", "c");

            if (c.size() == disksCount) {
                break;
            }

            makeMoves(a, b, "a", "b");
            makeMoves(c, b, "c", "b");
        }
    }

    private static void makeMoves(Deque<Integer> first, Deque<Integer> second, String label1, String label2) {

        if (first.isEmpty()) {
            first.push(second.pop());
            LOG.info(first.peek() + ": " + label2 + " -> " + label1);
        }

        else if (second.isEmpty()) {
            second.push(first.pop());
            LOG.info(second.peek() + ": " + label1 + " -> " + label2);
        }
        else {

            if (first.peek().compareTo(second.peek()) < 0) {
                second.push(first.pop());
                LOG.info(second.peek() + ": " + label1 + " -> " + label2);
            }
            else {
                first.push(second.pop());
                LOG.info(first.peek() + ": " + label2 + " -> " + label1);
            }

        }

    }

}
