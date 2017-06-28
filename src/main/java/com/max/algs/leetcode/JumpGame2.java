package com.max.algs.leetcode;

import org.apache.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Given an array of non-negative integers, you are initially positioned at the first index of the array.
 * <p>
 * Each element in the array represents your maximum jump length at that position.
 * <p>
 * Your goal is to reach the last index in the minimum number of jumps.
 * <p>
 * For example:
 * Given array A = [2,3,1,1,4]
 * <p>
 * The minimum number of jumps to reach the last index is 2. (Jump 1 step from index 0 to 1, then 3 steps to the last index.)
 */
public class JumpGame2 {

    private static final Logger LOG = Logger.getLogger(JumpGame2.class);

    private JumpGame2() throws Exception {

        int value = 25_000;

        int[] arr = new int[value + 3];

        for (int i = 0; i < arr.length; i++, value--) {
            arr[i] = (value > 0 ? value : 1);
        }

//        int[] arr = {4, 1, 1, 3, 1, 1, 1};

        long startTime = System.nanoTime();
        int jumps = jump(arr);
        long endTime = System.nanoTime();


        System.out.println("jumps: " + jumps);
        System.out.println("time: " + ((endTime - startTime) / 1_000_000) + " ms");

        System.out.println("Main done...");
    }

    public static void main(String[] args) {
        try {
            new JumpGame2();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    public int jumpSlow(int[] nums) {
        int[] d = new int[nums.length];
        int finalPos = d.length - 1;

        d[0] = 0;
        for (int i = 1; i < d.length; i++) {
            d[i] = Integer.MAX_VALUE;
        }
        int index;

        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = 1; j <= nums[i]; j++) {

                index = i + j;

                if (index == finalPos) {
                    return d[i] + 1;
                }

                d[index] = Math.min(d[index], d[i] + 1);
            }

        }

        return d[d.length - 1];
    }

    public int jump(int[] nums) {
        class IndexJump {
            final int index;
            final long jump;

            public IndexJump(int index, long jump) {
                this.index = index;
                this.jump = jump;
            }
        }

        Deque<IndexJump> stack = new ArrayDeque<>();
        stack.push(new IndexJump(nums.length - 1, 0));

        for (int i = nums.length - 2; i >= 0; i--) {

            int lastIndex = Math.min(nums.length - 1, i + nums[i]);
            long minDist = Integer.MAX_VALUE;

            for (IndexJump ij : stack) {
                if (ij.index <= lastIndex) {
                    minDist = Math.min(minDist, ij.jump + 1L);
                }
                else {
                    break;
                }
            }

            while (stack.peek().index < lastIndex) {
                if (stack.peek().jump >= minDist) {
                    stack.pop();
                }
                else {
                    break;
                }
            }

            stack.push(new IndexJump(i, minDist));
        }


        return (int) stack.pop().jump;
    }

}
