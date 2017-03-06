package com.max.algs.ds.list;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Single linked list implementation.
 * Not thread safe. Don't allow to store NULL values.
 */
public final class SingleLinkedList<T> {

    private static final Random RAND = new Random();

    private Node<T> head;
    private int size;

    public SingleLinkedList() {
    }

    /**
     * Use merge-sort like algorithm.
     * <p>
     * Perform the same procedure as merge sort. When merging, instead of selecting an element (one-by-one)
     * from the two lists in sorted order, flip a coin. Choose whether to pick an element from the first or
     * from the second list based on the result of the coin flip.
     * <p>
     * <p>
     * time: O(N*lgN)
     * space: O(1)
     */
    public void randomShuffle() {

        Queue<Node<T>> queue = new ArrayDeque<>();

        Node cur = head;

        while (cur != null) {

            Node<T> temp = cur.next;

            cur.next = null;
            queue.add(cur);

            cur = temp;
        }

        while (queue.size() != 1) {
            Node<T> first = queue.poll();
            Node<T> second = queue.poll();

            Node<T> merged = mergeInRandomOrder(first, second);

            queue.add(merged);
        }

        head = queue.poll();
    }

    /**
     * Merge two linked lists in random order.
     */
    private static <U> Node<U> mergeInRandomOrder(Node<U> list1, Node<U> list2) {

        Node<U> first = list1;
        Node<U> second = list2;

        Node<U> mergedHead;
        Node<U> last;

        if (RAND.nextBoolean()) {
            mergedHead = first;
            last = first;
            first = first.next;
        }
        else {
            mergedHead = second;
            last = second;
            second = second.next;
        }

        while (first != null || second != null) {

            boolean pickFromFirst = RAND.nextBoolean();

            if ((first != null && pickFromFirst) || second == null) {
                Node<U> temp = first.next;
                last.next = first;
                first = temp;
            }
            else {
                Node<U> temp = second.next;
                last.next = second;
                second = temp;
            }

            last = last.next;
        }

        return mergedHead;
    }

    public SingleLinkedList(Collection<T> col) {
        checkNotNull(col, "Can't construct linked list from 'null' collection");

        if (col.isEmpty()) {
            return;
        }

        Iterator<T> it = col.iterator();

        head = new Node<T>(it.next());
        Node cur = head;

        while (it.hasNext()) {
            cur.next = new Node<>(it.next());
            cur = cur.next;
        }
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        return size;
    }

    /**
     * Reverse single linked list in-place.
     * time: O(N)
     * space: O(1)
     */
    public void reverse() {
        Node<T> prev = null;
        Node<T> cur = head;
        Node<T> temp = null;

        while (cur != null) {
            temp = cur.next;
            cur.next = prev;
            prev = cur;
            cur = temp;
        }

        head = prev;
    }

    /**
     * Rearrange linked list in place, so that all 'even' index elements goes first and than all 'odd'.
     * <p>
     * time: O(N)
     * space: O(1)
     * <p>
     * Example: 1->2->3->4->5   became  1->3->5->2->4
     */
    public void rearrangeOddEven() {
        if (head == null || head.next == null || head.next.next == null) {
            return;
        }

        Node<T> evenHead = head.next;
        Node<T> even = evenHead;

        Node<T> cur = head;
        Node<T> prev = null;

        while (true) {

            prev = cur;

            if (cur.next == null) {
                break;
            }

            cur.next = cur.next.next;
            cur = cur.next;

            if (even.next == null) {
                break;
            }
            even.next = even.next.next;
            even = even.next;
        }

        prev.next = evenHead;
    }

    public void addFirst(T value) {
        checkNotNull(value, "Can't store NULL value");
        Node<T> newNode = new Node<T>(value, head);
        head = newNode;
        ++size;
    }

    public boolean deleteFirst() {
        if (head == null) {
            return false;
        }

        Node<T> tempHead = head.next;
        head.next = null;
        head = tempHead;
        --size;
        return true;
    }

    @Override
    public String toString() {

        StringBuilder buf = new StringBuilder();

        buf.append("[");

        Node<T> cur = head;

        while (cur != null) {

            buf.append(cur.value);

            if (cur.next != null) {
                buf.append(", ");
            }

            cur = cur.next;
        }

        buf.append("]");

        return buf.toString();
    }

    private static final class Node<U> {

        Node(U value, Node<U> next) {
            this.value = value;
            this.next = next;
        }

        Node(U value) {
            this(value, null);
        }

        final U value;
        Node<U> next;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Node<?> other = (Node<?>) obj;
            if (value == null) {
                if (other.value != null) {
                    return false;
                }
            }
            else if (!value.equals(other.value)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

    }

}
