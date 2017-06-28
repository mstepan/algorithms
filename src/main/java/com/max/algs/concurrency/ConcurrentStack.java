package com.max.algs.concurrency;

import akka.jsr166y.ThreadLocalRandom;
import org.apache.log4j.Logger;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author Maksym Stepanenko.
 */
public class ConcurrentStack {

    private static final Logger LOG = Logger.getLogger(ConcurrentStack.class);
    private static final Random RAND = ThreadLocalRandom.current();
    private final AtomicReference<Node> top = new AtomicReference<>(null);
    private final AtomicLong popEliminatedCount = new AtomicLong(0L);
    private final AtomicLong pushEliminatedCount = new AtomicLong(0L);
    @SuppressWarnings("unchecked")
    private final Exchanger<Integer>[] exchangers = new Exchanger[16];


    //	private static final Integer EMPTY = Integer.valueOf(0);
    private final boolean useElimination;

    public ConcurrentStack(boolean useElimination) {
        this.useElimination = useElimination;
        if (useElimination) {
            for (int i = 0; i < exchangers.length; i++) {
                exchangers[i] = new Exchanger<>();
            }
        }
    }

    public void printCounters() {
        LOG.info("PUSH eliminated: " + pushEliminatedCount.get());
        LOG.info("POP eliminated: " + popEliminatedCount.get());
    }

    private Integer visit(Integer valueToChange) throws TimeoutException {

        int exchIndex = RAND.nextInt(exchangers.length);

        try {
            return exchangers[exchIndex].exchange(valueToChange, 1, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException interEx) {
            Thread.currentThread().interrupt();
        }

        return null;
    }

    public void push(int value) {

        Node newNode = new Node(value, null);

        while (true) {
            if (tryPush(newNode)) {
                break;
            }
            else if (useElimination) {
                /** elimination */
                try {
                    Integer changedValue = visit(value);
                    if (changedValue == null) {
                        pushEliminatedCount.incrementAndGet();
                        break;
                    }
                }
                catch (TimeoutException timeoutEx) {
                }
            }
        }
    }

    private boolean tryPush(Node newNode) {
        Node oldNode = top.get();
        newNode.next = oldNode;
        return top.compareAndSet(oldNode, newNode);
    }


    public int pop() {

        while (true) {

            Node node = tryPop();

            if (node != null) {
                return node.value;
            }
            else if (useElimination) {
                /** elimination */
                try {
                    Integer changedValue = visit(null);

                    if (changedValue != null) {
                        popEliminatedCount.incrementAndGet();
                        return changedValue;
                    }
                }
                catch (TimeoutException timeoutEx) {
                }
            }
        }
    }

    private Node tryPop() {
        Node oldTop = top.get();

        if (oldTop == null) {
            return null;
        }

        Node newTop = oldTop.next;

        if (top.compareAndSet(oldTop, newTop)) {
            oldTop.next = null;
            return oldTop;
        }

        return null;
    }

    private static final class Node {

        final int value;
        Node next;

        Node(int value, Node next) {
            super();
            this.value = value;
            this.next = next;
        }


        @Override
        public String toString() {
            return String.valueOf(value);
        }


    }

}
