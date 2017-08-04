package com.max.algs.epi.recursion;


import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;


/**
 * Solve towers of Hanoii for 3 pegs and N rings.
 * <p>
 * N = rings count
 * <p>
 * time: O(2^N)
 * space: O(N)
 */
public final class TowersOfHanoii {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private static final class PegsMove {
        final Deque<Integer> src;
        final Deque<Integer> dest;
        final Deque<Integer> temp;
        final int count;

        PegsMove(Deque<Integer> src, Deque<Integer> dest, Deque<Integer> temp, int count) {
            this.src = src;
            this.dest = dest;
            this.temp = temp;
            this.count = count;
        }
    }


    private static void solveTowersOfHanoiiIterative() {
        Deque<Integer> srcPeg1 = new ArrayDeque<>();

        for (int i = 6; i != 0; --i) {
            srcPeg1.push(i);
        }

        printTowerState(srcPeg1);

        Deque<Integer> destPeg1 = new ArrayDeque<>();
        Deque<Integer> tempPeg1 = new ArrayDeque<>();

        Deque<PegsMove> commandsQueue = new ArrayDeque<>();
        commandsQueue.push(new PegsMove(srcPeg1, destPeg1, tempPeg1, srcPeg1.size()));

        while (!commandsQueue.isEmpty()) {

            PegsMove singleMove = commandsQueue.pop();

            Deque<Integer> src = singleMove.src;
            Deque<Integer> dest = singleMove.dest;
            Deque<Integer> temp = singleMove.temp;

            int count = singleMove.count;

            if (count == 1) {
                moveOneRing(src, dest);
                continue;
            }

            commandsQueue.push(new PegsMove(temp, dest, src, count - 1));
            commandsQueue.push(new PegsMove(src, dest, temp, 1));
            commandsQueue.push(new PegsMove(src, temp, dest, count - 1));
        }

        printTowerState(destPeg1);
    }

    private static void moveOneRing(Deque<Integer> src, Deque<Integer> dest) {
        assert dest.isEmpty() || dest.peek() > src.peek() : "Tower of Hanoii constraint violated";
        LOG.info("Moving " + src.peek() + " from " + src + " to " + dest);
        dest.push(src.pop());
    }

    public static void solveTowersOfHanoiiRec() {
        Deque<Integer> src = new ArrayDeque<>();

        for (int i = 6; i != 0; --i) {
            src.push(i);
        }

        printTowerState(src);

        Deque<Integer> dest = new ArrayDeque<>();
        Deque<Integer> temp = new ArrayDeque<>();

        towersOfHanoiiRec(src, dest, temp, src.size());

        printTowerState(dest);
    }

    private static void towersOfHanoiiRec(Deque<Integer> src, Deque<Integer> dest, Deque<Integer> temp, int count) {
        if (count == 1) {
            moveOneRing(src, dest);
            return;
        }

        towersOfHanoiiRec(src, temp, dest, count - 1);

        moveOneRing(src, dest);

        towersOfHanoiiRec(temp, dest, src, count - 1);
    }

    private static void printTowerState(Deque<Integer> data) {

        Iterator<Integer> descIt = data.descendingIterator();

        StringBuilder buf = new StringBuilder();

        while (descIt.hasNext()) {
            buf.append(descIt.next()).append(", ");
        }

        LOG.info(buf);
    }

    private TowersOfHanoii() {

        solveTowersOfHanoiiIterative();
        solveTowersOfHanoiiRec();

        LOG.info("TowersOfHanoii done: java-" + System.getProperty("java.version"));
    }


    public static void main(String[] args) {
        try {
            new TowersOfHanoii();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
