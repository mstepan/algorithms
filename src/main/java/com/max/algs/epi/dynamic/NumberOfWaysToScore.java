package com.max.algs.epi.dynamic;

import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.math3.util.MathUtils.checkNotNull;


public class NumberOfWaysToScore {


    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());


    /**
     * N -  finalScore
     * K - scores.length
     * <p>
     * time: O(exponential)
     * space: O(N)
     */
    private static int numberOfWaysBruteforce(int[] scores, int finalScore) {
        checkPreconditions(scores, finalScore);
        return numberOfWaysRec(scores, finalScore, scores.length - 1);
    }

    private static int numberOfWaysRec(int[] scores, int finalScore, int lastScoreIndex) {

        if (lastScoreIndex < 0) {
            return 0;
        }

        int curScore = scores[lastScoreIndex];

        int scoreIncluded = 0;

        // with 'curScore'
        if (curScore == finalScore) {
            scoreIncluded = 1;
        }
        else if (curScore < finalScore) {
            scoreIncluded = numberOfWaysRec(scores, finalScore - curScore, lastScoreIndex);
        }

        // without 'curScore'
        int scoreExcluded = numberOfWaysRec(scores, finalScore, lastScoreIndex - 1);

        return scoreIncluded + scoreExcluded;
    }

    /**
     * N - finalScore
     * K - scores.length
     * <p>
     * time: O(N * K)
     * space: O(N * K)
     */
    private static int numberOfWaysDynamic(int[] scores, int finalScore) {

        checkPreconditions(scores, finalScore);

        if (noSolution(scores, finalScore)) {
            return 0;
        }


        int[][] sol = new int[scores.length][finalScore + 1];

        // set initial values
        for (int i = 0; i < scores.length; ++i) {
            sol[i][scores[i]] = 1;
        }

        for (int row = 0; row < sol.length; ++row) {
            for (int col = 0; col < sol[row].length; ++col) {

                int curScore = scores[row];

                // without 'curScore'
                if (row != 0) {
                    sol[row][col] += sol[row - 1][col];
                }


                // with 'sol[row][col]'
                if (curScore <= col) {
                    sol[row][col] += sol[row][col - curScore];
                }
            }
        }

        int lastRow = sol.length - 1;
        int lastCol = sol[lastRow].length - 1;

        return sol[lastRow][lastCol];
    }

    private static boolean noSolution(int[] scores, int finalScore) {
        if (scores.length == 0 || finalScore == 0) {
            return true;
        }

        // check at leats one score is less
        for (int singleScore : scores) {
            if (singleScore <= finalScore) {
                return false;
            }
        }

        return true;
    }

    private static void checkPreconditions(int[] scores, int finalScore) {
        checkNotNull(scores);
        checkArgument(finalScore >= 0);

        for (int singleScore : scores) {
            checkArgument(singleScore > 0);
        }
    }


    /**
     * N - finalScore
     * K - scores.length
     * <p>
     * time: O(N * K)
     * space: O(N)
     */
    private static int numberOfWaysDynamicOptimized(int[] scores, int finalScore) {

        checkPreconditions(scores, finalScore);

        if (finalScore == 0 || scores.length == 0) {
            return 0;
        }

        int[] sol = new int[finalScore + 1];
        sol[0] = 1;

        for (int curScore : scores) {
            for (int val = curScore; val < sol.length; ++val) {
                if (curScore <= val) {
                    sol[val] += sol[val - curScore];
                }
            }
        }

        return sol[sol.length - 1];
    }


    private NumberOfWaysToScore() {

        int[] scores = {2, 3, 7};
        int finalScore = 133;

        LOG.info("numberOfWaysBruteforce: " + numberOfWaysBruteforce(scores, finalScore));
        LOG.info("numberOfWaysDynamic: " + numberOfWaysDynamic(scores, finalScore));
        LOG.info("numberOfWaysDynamicOptimized: " + numberOfWaysDynamicOptimized(scores, finalScore));

        LOG.info("NumberOfWaysToScore done...");
    }


    public static void main(String[] args) {
        try {
            new NumberOfWaysToScore();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
