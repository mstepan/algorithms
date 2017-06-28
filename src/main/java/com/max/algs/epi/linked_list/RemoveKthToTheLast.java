package com.max.algs.epi.linked_list;


import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class RemoveKthToTheLast {

    private static final Logger LOG = Logger.getLogger(RemoveKthToTheLast.class);

    private RemoveKthToTheLast() throws Exception {

        Node list = Node.createList(1, 2, 3, 4, 5, 6, 7);
        int k = 7;

        System.out.println(list);

        list = removeKthToTheLast(list, k);
        System.out.println(list);

        System.out.printf("RemoveKthToTheLast done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * time: O(N)
     * space: O(1)
     */
    private static Node removeKthToTheLast(Node head, int k) {

        checkNotNull(head);
        checkArgument(k >= 0);

        Node skip = head;

        for (int i = 0; i <= k + 1; ++i) {
            if (skip == null) {
                // delete head
                if (i == k + 1) {
                    Node newHead = head.next;

                    // null head next pointer to reduce memory leak
                    head.next = null;
                    return newHead;
                }
                else {
                    throw new IllegalArgumentException(
                            String.format("'k = %s' is out of range [%s, %s]", k, 0, i - 1));
                }
            }
            else {
                skip = skip.next;
            }
        }

        Node cur = head;
        while (skip != null) {
            skip = skip.next;
            cur = cur.next;
        }

        Node temp = cur.next;
        cur.next = temp.next;
        // null temp next pointer to reduce memory leak
        temp.next = null;

        return head;
    }

    public static void main(String[] args) {
        try {
            new RemoveKthToTheLast();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
