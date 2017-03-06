package com.max.algs.backtracking;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * See "Elements of programming interviews" 17.4
 */
public class MeasuringWithDefectiveJugs {


    private void findSolution(int[] jugs, int minValue, int maxValue, int minReq, int maxReq, Deque<Integer> res) {

        if (minValue > maxReq) {
            return;
        }

        if (minValue >= minReq && maxValue <= maxReq) {
            System.out.println(formatSolution(res, minReq, maxReq));
            return;
        }

        for (int i = 0; i < jugs.length; i += 2) {

            int minCur = jugs[i];
            int maxCur = jugs[i + 1];

            res.add(minCur);
            res.add(maxCur);

            findSolution(jugs, minValue + minCur, maxValue + maxCur, minReq, maxReq, res);

            res.pollLast();
            res.pollLast();
        }
    }

    private static String formatSolution(Deque<Integer> res, int minReq, int maxReq) {

        int totalMin = 0;
        int totalMax = 0;

        StringBuilder buf = new StringBuilder();
        Iterator<Integer> it = res.iterator();

        while (it.hasNext()) {

            int minValue = it.next();
            int maxValue = it.next();

            totalMin += minValue;
            totalMax += maxValue;

            buf.append("(").append(minValue).append(";").append(maxValue).append(")");
        }

        buf.append("totalMin = ").append(totalMin).append(", totalMax = ").append(totalMax);

        if (totalMin < minReq || totalMax > maxReq) {
            throw new IllegalStateException("Incorrect value detecetd");
        }


        return buf.toString();
    }


    public MeasuringWithDefectiveJugs() throws Exception {

        int[] jugs = {230, 240, 290, 310, 500, 515};

        final int minRequired = 2100;
        final int maxRequired = 2300;

        findSolution(jugs, 0, 0, minRequired, maxRequired, new ArrayDeque<>());

        System.out.println("Main done...");
    }


    public static void main(String[] args) {
        try {
            new MeasuringWithDefectiveJugs();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
