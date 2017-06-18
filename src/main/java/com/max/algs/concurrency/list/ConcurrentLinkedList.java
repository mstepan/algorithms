package com.max.algs.concurrency.list;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Fine grained concurrent linked list.
 * Use lock coupling paradigm to reduce thread lock contention.
 *
 * @author Maksym Stepanenko
 */
public class ConcurrentLinkedList {

    private final Node tail = new Node(0);
    private final Node head = new Node(0, tail);

    private final AtomicInteger size = new AtomicInteger(0);


    public int size() {
        return size.get();
    }


    public boolean contains(int value) {

        Node pred = null;
        Node cur = head;
        cur.lock();

        try {
            while (cur.value != value && cur != tail) {

                if (pred != null) {
                    pred.unlock();
                }

                pred = cur;
                cur = cur.next;
                cur.lock();
            }

            if (cur != tail) {
                return true;
            }
            return false;
        }
        finally {
            cur.unlock();
            if (pred != null) {
                pred.unlock();
            }
        }
    }

    /**
     * Add to the beginning of the list.
     */
    public void add(int value) {

        head.lock();
        head.next.lock();

        Node headNext = head.next;

        try {
            Node newNode = new Node(value);
            head.next = newNode;
            newNode.next = headNext;

            size.incrementAndGet();
        }
        finally {
            headNext.unlock();
            head.unlock();
        }
    }

    /**
     * Remove first occurrence of an element from the list.
     * Returns 'true' if elements was removed, 'false' - otherwise.
     */
    public boolean remove(int value) {

        Node pred = null;
        Node cur = head;

        cur.lock();

        try {
            while (cur.value != value) {

                if (pred != null) {
                    pred.unlock();
                }

                pred = cur;
                cur = cur.next;

                cur.lock();
            }

            if (cur.value == value) {
                pred.next = cur.next;
                cur.next = null;
                size.decrementAndGet();
                return true;
            }
            return false;
        }
        finally {
            cur.unlock();
            if (pred != null) {
                pred.unlock();
            }
        }
    }

    @Override
    public String toString() {

        Node pred = head;
        pred.lock();
        Node cur = head.next;
        cur.lock();

        StringBuilder buf = new StringBuilder();


        try {
            while (cur != tail) {

                if (pred != head) {
                    buf.append(", ");
                }
                buf.append(cur.value);

                pred.unlock();
                pred = cur;

                cur = cur.next;
                cur.lock();
            }
        }
        finally {
            cur.unlock();
            pred.unlock();
        }

        return buf.toString();
    }


    private static class Node {
        final int value;
        final Lock mutex = new ReentrantLock();
        Node next;

        Node(int value) {
            this(value, null);
        }

        Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }

        void lock() {
            mutex.lock();
        }

        void unlock() {
            mutex.unlock();
        }

        @Override
        public String toString() {
            if (value == Integer.MIN_VALUE) {
                return "head";
            }

            if (value == Integer.MAX_VALUE) {
                return "tail";
            }

            return String.valueOf(value);
        }


    }

}
