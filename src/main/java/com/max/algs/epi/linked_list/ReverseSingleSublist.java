package com.max.algs.epi.linked_list;


import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkArgument;


public class ReverseSingleSublist {

    private static final Logger LOG = Logger.getLogger(ReverseSingleSublist.class);

    private ReverseSingleSublist() throws Exception {

        Node list = Node.createList(11, 7, 5, 3, 2);
        System.out.println(list);

        final int from = 2;
        final int to = 4;

        list = reverseSublist(list, from, to);
        System.out.println(list);

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * 8.2. Reverse a single sublist.
     * <p>
     * Reverse sublist of a single linked list.
     * We need to return a new 'head' value, because we can reverse the list starting from 'head'.
     * <p>
     * time: O(N), N = to
     * space: O(1)
     */
    private static Node reverseSublist(Node head, int from, int to) {
        checkArgument(head != null, "null 'head' value passed");
        checkArgument(from >= 1, "incorrect 'from' value %s", from);
        checkArgument(to >= 1, "incorrect 'to' value %s", to);
        checkArgument(from <= to, "from > to: %s > %s", from, to);
        checkArgument(checkListHasIndex(head, to - 1), "list too small to revert sublist till %s", to);

        if (from == to) {
            return head;
        }

        // add sentinel object,
        // this will simplify handling case when 'from' is pointing to head.
        Node sentinel = new Node(Integer.MIN_VALUE);
        sentinel.next = head;
        head = sentinel;

        Node subHead = head;

        for (int i = 0; i < from - 1; ++i) {
            subHead = subHead.next;
            checkArgument(subHead != null, "list is too small to reverse from = %s", from);
        }

        final int elemsToRevert = to - from;
        Node tail = subHead.next;

        for (int i = 0; i < elemsToRevert; ++i) {
            // delete after 'tail'
            Node cur = tail.next;
            tail.next = cur.next;
//            cur.next = null; // ???

            // insert after 'subHead'
            cur.next = subHead.next;
            subHead.next = cur;
        }


        // remove sentinel object
        head = sentinel.next;
        sentinel.next = null;

        return head;
    }

    private static boolean checkListHasIndex(Node head, int index) {

        int curIndex = 0;
        for (Node cur = head; cur != null; cur = cur.next, ++curIndex) {
            if (curIndex == index) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        try {
            new ReverseSingleSublist();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
