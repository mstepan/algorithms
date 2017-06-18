package com.max.algs.leetcode;


import java.util.*;

public class Solution {

    public Solution() throws Exception {

        int[] nums = {1, 1, 2};

        List<List<Integer>> allSolutions = permuteUnique(nums);

        for (List<Integer> singleSolution : allSolutions) {
            System.out.println(singleSolution);
        }


        System.out.println("Main done...");
    }

    public static void main(String[] args) {
        try {
            new Solution();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<List<Integer>> permuteUnique(int[] nums) {

        List<List<Integer>> allSolutions = new ArrayList<>();

        genPerm(nums, new BitSet(nums.length), new ArrayDeque<>(nums.length), allSolutions);

        return allSolutions;
    }

    private void genPerm(int[] arr, BitSet used, Deque<Integer> res, List<List<Integer>> allSolutions) {

        if (res.size() == arr.length) {
            allSolutions.add(new ArrayList<>(res));
            return;
        }

        Set<Integer> usedInPos = new HashSet<>();

        for (int i = 0; i < arr.length; i++) {

            int cur = arr[i];

            if (!used.get(i) && !usedInPos.contains(cur)) {

                usedInPos.add(cur);
                used.set(i);
                res.add(cur);

                genPerm(arr, used, res, allSolutions);

                res.pollLast();
                used.clear(i);

            }
        }

    }

}
