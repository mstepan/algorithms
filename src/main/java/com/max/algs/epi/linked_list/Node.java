package com.max.algs.epi.linked_list;

class Node {

    Node next;
    int value;

    Node(Node next, int value) {
        this.next = next;
        this.value = value;
    }

    Node(int value) {
        this(null, value);
    }

    static Node createList(Integer... values) {
        if (values.length == 0) {
            return null;
        }

        Node head = new Node(values[0]);
        Node tail = head;

        for (int i = 1; i < values.length; ++i) {
            tail.next = new Node(values[i]);
            tail = tail.next;
        }

        return head;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();

        buf.append("[");
        buf.append(this.value);

        for (Node cur = this.next; cur != null; cur = cur.next) {
            buf.append(", ").append(cur.value);
        }
        buf.append("]");

        return buf.toString();
    }

}
