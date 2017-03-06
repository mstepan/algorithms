package com.max.algs.leetcode;

/**
 * Given a linked list, reverse the nodes of a linked list k at a time and return its modified list.
 * <p>
 * If the number of nodes is not a multiple of k then left-out nodes in the end should remain as it is.
 * <p>
 * You may not alter the values in the nodes, only nodes itself may be changed.
 * <p>
 * Only constant memory is allowed.
 * <p>
 * For example,
 * Given this linked list: 1->2->3->4->5
 * <p>
 * For k = 2, you should return: 2->1->4->3->5
 * <p>
 * For k = 3, you should return: 3->2->1->4->5
 * <p>
 * see: https://leetcode.com/problems/reverse-nodes-in-k-group/
 */
public class ReverseNodesInKGroup {

    public ListNode reverseKGroup(ListNode head, int k) {

        if (k == 0) {
            return head;
        }

        ListNode newHead = head;


        //TODO:
        return newHead;
    }


    public ReverseNodesInKGroup() throws Exception {

        ListNode head = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5)))));
        int k = 2;

        System.out.println(head);

        reverseKGroup(head, k);

        System.out.println(head);

    }


    public static void main(String[] args) {
        try {
            new ReverseNodesInKGroup();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }

        ListNode(int value, ListNode next) {
            this.val = value;
            this.next = next;
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();

            ListNode cur = this;

            buf.append(cur.val);
            cur = cur.next;

            while (cur != null) {
                buf.append("->").append(cur.val);
                cur = cur.next;
            }

            return buf.toString();
        }
    }

}
