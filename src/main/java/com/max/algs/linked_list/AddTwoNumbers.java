package com.max.algs.linked_list;

/**
 * You are given two linked lists representing two non-negative numbers. The digits are stored in reverse
 * order and each of their nodes contain a single digit. Add the two numbers and return it as a linked list.
 *
 * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
 * Output: 7 -> 0 -> 8
 *
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
public class AddTwoNumbers {


    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int value, ListNode next) {
            this.val = value;
            this.next = next;
        }

        ListNode(int value) {
            this(value, null);
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();


            buf.append(val);

            ListNode cur = next;

            while (cur != null) {
                buf.append("->").append(cur.val);
                cur = cur.next;
            }
            return buf.toString();
        }
    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        final int base = 10;
        ListNode first = l1;
        ListNode second = l2;

        int carry = 0;
        int digit;

        ListNode res = null;
        ListNode prev = null;
        ListNode cur;

        while (first != null || second != null) {

            int digit1 = first != null ? first.val : 0;
            int digit2 = second != null ? second.val : 0;

            int sum = digit1 + digit2 + carry;

            digit = sum % base;
            carry = sum / base;

            cur = new ListNode(digit);

            if (prev == null) {
                res = cur;
            }
            else {
                prev.next = cur;
            }

            prev = cur;

            if (first != null) {
                first = first.next;
            }
            if (second != null) {
                second = second.next;
            }
        }

        if( carry != 0 ){
            prev.next = new ListNode(carry);
        }

        return res;
    }


    public static void main(String[] args) {
        // (2 -> 4 -> 3) + (5 -> 6 -> 4)

        ListNode first = new ListNode(5, new ListNode(3, new ListNode(9)));
        ListNode second = new ListNode(4, new ListNode(8));

        ListNode res = addTwoNumbers(first, second);
        System.out.println(res);
    }

}
