package com.max.algs.epi.linked_list;


import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class ReverseKNodes {

    private static final Logger LOG = Logger.getLogger(ReverseKNodes.class);

    private ReverseKNodes() throws Exception {

        Node list = Node.createList(1, 2, 3, 4, 5, 6, 7);
        int k = 3;

        System.out.println(list);

        list = reverseKNodes(list, k);
        System.out.println(list);

        System.out.printf("ReverseKNodes done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * 8.2. Variant.
     * <p>
     * Reverse the list 'k' nodes at a time. Do not reverse the last 'list.length % k' nodes.
     * <p>
     * time: O(N)
     * space: O(1)
     */
    private static Node reverseKNodes(Node head, int k) {

        checkNotNull(head);
        checkArgument(k >= 0);

        if (k < 2) {
            return head;
        }

        final Node sentinel = new Node(head, Integer.MIN_VALUE);

        Node pred = sentinel;
        Node succ = head;

        MAIN:
        while (true) {

            for (int i = 0; i < k; ++i) {
                if (succ == null) {
                    break MAIN;
                }

                succ = succ.next;
            }

            pred = reverseSublist(pred, succ);
        }

        Node newHead = sentinel.next;
        sentinel.next = null;

        return newHead;
    }

    /**
     * @return last node in reversed list.
     */
    private static Node reverseSublist(Node pred, Node succ) {
        Node prev = pred;
        Node cur = pred.next;
        Node first = cur;
        Node temp;

        while (cur != succ) {
            temp = cur.next;
            cur.next = prev;
            prev = cur;
            cur = temp;
        }

        pred.next = prev;
        first.next = cur;

        return first;
    }

    public static void main(String[] args) {
        try {
            new ReverseKNodes();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
