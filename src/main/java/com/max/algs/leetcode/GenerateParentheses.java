package com.max.algs.leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


public class GenerateParentheses {


    public List<String> generateParenthesis(int n) {
        List<String> solutions = new ArrayList<>();
        genRec(0, 0, n, new ArrayDeque<>(), solutions);
        return solutions;
    }

    private String createResultString(Deque<Character> singleRes) {
        StringBuilder buf = new StringBuilder();

        for (Character ch : singleRes) {
            buf.append(ch);
        }

        return buf.toString();
    }

    private void genRec(int leftCnt, int rightCnt, int pairsCount, Deque<Character> singleRes, List<String> solutions) {

        if (leftCnt == pairsCount && rightCnt == pairsCount) {
            solutions.add(createResultString(singleRes));
            return;
        }

        if (leftCnt < pairsCount) {
            singleRes.add('(');
            genRec(leftCnt + 1, rightCnt, pairsCount, singleRes, solutions);
            singleRes.pollLast();
        }

        if (rightCnt < leftCnt) {
            singleRes.add(')');
            genRec(leftCnt, rightCnt + 1, pairsCount, singleRes, solutions);
            singleRes.pollLast();
        }
    }


    private GenerateParentheses() throws Exception {

        int n = 3;
        List<String> solutions = generateParenthesis(n);

        for (String singlSolution : solutions) {
            System.out.println(singlSolution);
        }

        System.out.println("Main done...");
    }

    public static void main(String[] args) {
        try {
            new GenerateParentheses();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
