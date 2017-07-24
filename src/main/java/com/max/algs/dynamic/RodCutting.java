package com.max.algs.dynamic;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Given an iron road of a certain length and price of selling roads of
 * different lengths in the market, how should we cut the road so that the
 * profit is maximized.
 */
public class RodCutting {


    private static final Logger LOG = Logger.getLogger(RodCutting.class);

    /**
     * Dynamic programming solution.
     * <p>
     * K = length
     * N = pieces.length
     * <p>
     * time: O(K*N)
     * space: O(K)
     */
    public static int maxProfitDynamic(int[] pieces, int[] cost, int length) {
        checkPreconditions(pieces, cost, length);

        int[] optCost = new int[length + 1];

        for (int i = 1; i < optCost.length; ++i) {

            optCost[i] = Integer.MIN_VALUE;

            for (int j = 0; j < pieces.length; ++j) {

                int pieceLength = pieces[j];
                int pieceCost = cost[j];

                if (pieceLength <= i) {
                    optCost[i] = Math.max(optCost[i], pieceCost + optCost[i - pieceLength]);
                }
            }
        }

        // reconstruct solution, time: O(N*K), space: O(N)
        List<Integer> cutPieces = new ArrayList<>();

        int index = optCost.length - 1;

        while (index != 0) {

            for (int j = 0; j < pieces.length; ++j) {
                int pieceLength = pieces[j];
                int pieceCost = cost[j];

                if (pieceLength <= index && optCost[index] == pieceCost + optCost[index - pieceLength]) {
                    cutPieces.add(pieceLength);
                    index -= pieceLength;
                }
            }
        }
        cutPieces.sort(Collections.reverseOrder());

        LOG.info("maxProfitDynamic: " + cutPieces);

        return optCost[optCost.length - 1];
    }

    private static void checkPreconditions(int[] pieces, int[] cost, int length) {
        checkNotNull(pieces, "pieces array is null");
        checkNotNull(cost, "cost array is null");
        checkArgument(length >= 0, "length = %s, should be positive or zero", length);
        checkArgument(pieces.length == cost.length);
    }

    /**
     * Recursive solution without memoization.
     * <p>
     * time: ~ O(N^K), exponential
     * space: O(K)
     */
    public static int maxProfitBruteforce(int[] pieces, int[] cost, int length) {
        checkPreconditions(pieces, cost, length);

        CutPart res = maxProfitRec(pieces, cost, length);

        res.pieces.sort(Collections.reverseOrder());

        LOG.info("maxProfitBruteforce: " + res.pieces);

        return res.totalPrice;
    }


    private static CutPart maxProfitRec(int[] pieces, int[] cost, int length) {

        assert length >= 0 : "Negative 'length' detected";

        if (length == 0) {
            return CutPart.EMPTY;
        }

        CutPart maxProfit = CutPart.MIN_VALUE;

        for (int i = 0; i < pieces.length; ++i) {

            int singlePiece = pieces[i];

            if (singlePiece <= length) {

                CutPart temp = maxProfitRec(pieces, cost, length - singlePiece);

                List<Integer> tempPieces = new ArrayList<>(temp.pieces);
                tempPieces.add(singlePiece);

                CutPart curProfit = new CutPart(tempPieces, temp.totalPrice + cost[i]);

                if (maxProfit.totalPrice < curProfit.totalPrice) {
                    maxProfit = curProfit;
                }
            }
        }

        return maxProfit;
    }

    private static final class CutPart {

        static final CutPart EMPTY = new CutPart(Collections.emptyList(), 0);
        static final CutPart MIN_VALUE = new CutPart(Collections.emptyList(), Integer.MIN_VALUE);

        final List<Integer> pieces;
        final int totalPrice;

        CutPart(List<Integer> pieces, int totalPrice) {
            this.pieces = pieces;
            this.totalPrice = totalPrice;
        }
    }

    private RodCutting() {

        int[] pieces = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] cost = {1, 5, 8, 9, 10, 17, 17, 20};

        int length = 17;

        LOG.info("maxProfitRec: " + maxProfitBruteforce(pieces, cost, length));
        LOG.info("maxProfitDynamic: " + maxProfitDynamic(pieces, cost, length));

        LOG.info("Main done: java-" + System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new RodCutting();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
