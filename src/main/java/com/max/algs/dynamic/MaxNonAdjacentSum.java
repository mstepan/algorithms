package com.max.algs.dynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 
 suppose you are given an array of int. for example A[ ] = {1, - 1, 3, 8 ,4 } 
 maximize the sum of subset such that if you include the number in sum, you may 
 not use adjutant numbers to count in sum. so in the example above the max sum is 1+ 8 = 9; 
in A[ ] = { 1, 5, 3, 9, 4 } the max sum is 5 + 9 = 14. 
and in A[ ] = { 1,2,3,4, 5} max sum i s 1 + 3 + 5 = 9
  
 */
public class MaxNonAdjacentSum {


    /**
     * time: O(n)
     * space: O(n)
     */
    public static List<Integer> findMaxSum(int[] arr) {

        List<Integer> maxSumList = new ArrayList<>();

        int[] sum = new int[arr.length];

        sum[0] = arr[0];
        sum[1] = Math.max(sum[0], arr[1]);

        for (int i = 2; i < arr.length; i++) {
            sum[i] = Math.max(sum[i - 1], arr[i] + sum[i - 2]);
        }

        int i = sum.length - 1;

        int recSum = sum[sum.length - 1];

        while (i > 0) {
            if (sum[i] != sum[i - 1]) {
                maxSumList.add(arr[i]);
                recSum -= arr[i];
                i -= 2;
            }
            else {
                --i;
            }
        }

        if (recSum > 0) {
            maxSumList.add(recSum);
        }

        Collections.reverse(maxSumList);

        return maxSumList;
    }

}
