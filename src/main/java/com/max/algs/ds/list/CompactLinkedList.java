package com.max.algs.ds.list;


/**
 * Store only one pointer to the HEAD.
 */
public class CompactLinkedList<T> {


    private Node<T> head;


    public void shuffleFromMiddle() {

        Node<T> slow = head;
        Node<T> fast = head;


        while (fast != null && fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // 'slow' points to the middle of a list

        if (slow == head) {
            return;
        }


        Node<T> cur = head;
        Node<T> mid = slow;

        while (cur != mid) {

            Node<T> temp = mid.next;
            mid.next = mid.next.next;
            temp.next = null;


            temp.next = cur.next;
            cur.next = temp;
            cur = cur.next.next;
        }

    }


    /**
     * Add new value to head.
     */
    public void add(T newValue) {
        if (head == null) {
            head = new Node<T>(newValue);
        }
        else {
            head = new Node<T>(newValue, head);
        }
    }

    /**
     * Delete value from head.
     */
    public T delete() {

        if (head == null) {
            return null;
        }

        Node<T> ret = head;

        head = head.next;
        ret.next = null;

        return ret.value;
    }

    @Override
    public String toString() {

        StringBuilder buf = new StringBuilder();

        buf.append("[");


        Node<T> cur = head;


        while (cur != null) {

            if (cur != head) {
                buf.append(", ");
            }

            buf.append(cur.value);
            cur = cur.next;
        }

        buf.append("]");

        return buf.toString();

    }

    private static class Node<U> {

        U value;
        Node<U> next;

        Node(U value) {
            this(value, null);
        }

        Node(U value, Node<U> next) {
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
