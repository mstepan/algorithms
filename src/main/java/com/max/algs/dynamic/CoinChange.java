package com.max.algs.dynamic;

import com.max.algs.util.ArrayUtils;
import org.apache.log4j.Logger;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


public class CoinChange {


    private static final Logger LOG = Logger.getLogger(CoinChange.class);

    /**
     * K = sum
     * N = coins.length
     * <p>
     * time: O(K*N)
     * space: O(K)
     */
    public static List<Integer> coinChangeDynamic(int[] coins, int sum) {
        int[] sol = new int[sum + 1];
        Arrays.fill(sol, 1, sol.length, Integer.MAX_VALUE);

        for (int i = 1; i < sol.length; ++i) {

            for (int coinVal : coins) {
                if (coinVal <= i) {
                    sol[i] = Math.min(sol[i], 1 + sol[i - coinVal]);
                }
            }
        }


        // reconstruct solution, time: O(N*K), space: O(1)
        List<Integer> change = new ArrayList<>(sol[sol.length - 1]);

        int index = sol.length - 1;

        while (index != 0) {

            for (int coinVal : coins) {

                if (coinVal <= index && (sol[index - coinVal] + 1) == sol[index]) {

                    change.add(coinVal);

                    index = index - coinVal;
                    break;
                }
            }
        }

        return change;
    }

    public static List<Integer> coinChangeGreedy(int[] coins, int initialSum) {

        // sort in ASC order
        Arrays.parallelSort(coins);

        // make sorted in DESC order
        reverseArray(coins);

        List<Integer> change = new ArrayList<>();
        int sum = initialSum;

        while (sum > 0) {

            if (coins[coins.length - 1] > sum) {
                throw new IllegalStateException("Can't make change using greedy approach");
            }

            for (int singleCoin : coins) {
                if (singleCoin <= sum) {
                    change.add(singleCoin);
                    sum -= singleCoin;
                    break;
                }
            }
        }

        return change;

    }

    private static void reverseArray(int[] arr) {
        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            ArrayUtils.swap(arr, left, right);
            ++left;
            --right;
        }
    }


    /**
     * K = 'sum'
     * N = coins.length
     * <p>
     * time: O(K*N)
     * space: O(K)
     */
    public static List<Integer> coinChangeMemoization(int[] coins, int sum) {
        checkNotNull(coins);
        checkArgument(sum >= 0);

        Arrays.sort(coins);

        ChangeRes res = coinChangeRec(coins, sum, new HashMap<>());

        return res.changeCoins;
    }

    private static ChangeRes coinChangeRec(int[] sortedCoins, int sum, Map<Integer, ChangeRes> cache) {

        if (cache.containsKey(sum)) {
            return cache.get(sum);
        }

        if (sum == 0) {
            return ChangeRes.EMPTY;
        }

        ChangeRes minRes = ChangeRes.INFINITY;

        for (int singleCoin : sortedCoins) {

            if (singleCoin > sum) {
                break;
            }

            ChangeRes curRes = coinChangeRec(sortedCoins, sum - singleCoin, cache);

            if (curRes.length() + 1 < minRes.length()) {
                List<Integer> temp = new ArrayList<>(curRes.changeCoins);
                temp.add(singleCoin);
                minRes = new ChangeRes(temp);
            }
        }

        cache.put(sum, minRes);

        return minRes;
    }

    private static final class ChangeRes {

        static final ChangeRes EMPTY = new ChangeRes(Collections.emptyList());
        static final ChangeRes INFINITY = new ChangeRes(Collections.emptyList(), Integer.MAX_VALUE);

        final List<Integer> changeCoins;
        final int length;

        ChangeRes(List<Integer> changeCoins) {
            this(changeCoins, changeCoins.size());
        }

        ChangeRes(List<Integer> changeCoins, int length) {
            this.changeCoins = changeCoins;
            this.length = length;
        }

        int length() {
            return length;
        }
    }

    private CoinChange() {

//        int[] coins = {1, 2, 5, 10, 20, 50};
//        int sum = 65;

//        int[] coins = {1, 5, 6, 9};
//        int sum = 11;

        int[] coins = {1, 2, 5, 10, 12, 20, 50};
        int sum = 65;

        LOG.info("coinChangeBruteforce: " + coinChangeMemoization(coins, sum));
        LOG.info("coinChangeGreedy: " + coinChangeGreedy(coins, sum));
        LOG.info("coinChangeDynamic: " + coinChangeDynamic(coins, sum));

        LOG.info("Main done: java-" + System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new CoinChange();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
