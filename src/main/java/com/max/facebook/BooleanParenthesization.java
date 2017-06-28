package com.max.facebook;

import com.google.common.base.Objects;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Given a boolean expression with following symbols.
 * Symbols
 * 'T' ---> true
 * 'F' ---> false
 * And following operators filled between symbols
 * Operators
 * &   ---> boolean AND
 * |   ---> boolean OR
 * ^   ---> boolean XOR
 * <p>
 * Count the number of ways we can parenthesize the expression so that the value of expression evaluates to true.
 * <p>
 * For Example:
 * The expression is "T | T & F ^ T", it evaluates true
 * in 4 ways ((T|T)&(F^T)), (T|(T&(F^T))), (((T|T)&F)^T)
 * and (T|((T&F)^T)).
 * <p>
 * Return No_of_ways Mod 1003.
 */
public class BooleanParenthesization {

    private static final Logger LOG = Logger.getLogger(BooleanParenthesization.class);

    private static final char TRUE_CH = 'T';
    private static final char FALSE_CH = 'F';

    private static final int MOD = 1003;

    private BooleanParenthesization() throws Exception {

        String expr = "T | T & F ^ T";
        int res = calculatePossibleTrueValues(expr);

        System.out.printf("res = %d %n", res);

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * N - true/false variables count in 'exprStr'
     * <p>
     * time: O(N^3)
     * space: O(N^2)
     */
    private static int calculatePossibleTrueValues(String exprStr) {
        checkNotNull(exprStr);

        final char[] expr = toCharArrayWithoutSpaces(exprStr);
        final int variablesCnt = (expr.length / 2) + 1;

        TFCounter[][] sol = new TFCounter[variablesCnt][variablesCnt];

        for (int row = 0; row < variablesCnt; ++row) {
            char ch = expr[row * 2];

            assert ch == TRUE_CH || ch == FALSE_CH : "incorrect T/F value: " + ch;

            sol[row][row] = TFCounter.create(ch);
        }

        for (int row = variablesCnt - 2; row >= 0; --row) {
            for (int col = row + 1; col < variablesCnt; ++col) {
                sol[row][col] = calculateCounter(sol, row, col, expr);
            }
        }

        return sol[0][variablesCnt - 1].trueCnt;
    }

    private static char[] toCharArrayWithoutSpaces(String str) {

        int cnt = 0;
        for (int i = 0, strLength = str.length(); i < strLength; ++i) {
            if (str.charAt(i) != ' ') {
                ++cnt;
            }
        }

        char[] res = new char[cnt];

        int index = 0;
        char ch;

        for (int i = 0, strLength = str.length(); i < strLength; ++i) {
            ch = str.charAt(i);
            if (ch != ' ') {
                res[index] = ch;
                ++index;
            }
        }

        return res;
    }

    private static TFCounter calculateCounter(TFCounter[][] sol, int row, int col, char[] expr) {

        int trueTotal = 0;
        int falseTotal = 0;

        for (int to = row; to < col; ++to) {
            TFCounter left = sol[row][to];
            TFCounter right = sol[to + 1][col];

            Operation op = Operation.fromChar(expr[(to * 2) + 1]);

            int[] counters = calculateForOperation(op, left, right);

            trueTotal = (trueTotal + counters[0]) % MOD;
            falseTotal = (falseTotal + counters[1]) % MOD;
        }

        return new TFCounter(trueTotal, falseTotal);
    }

    private static int[] calculateForOperation(Operation op, TFCounter left, TFCounter right) {

        int trueCnt = 0;
        int falseCnt = 0;

        for (BooleanExpr expr : op.table) {

            int curCnt = (expr.left ? left.trueCnt : left.falseCnt) * (expr.right ? right.trueCnt : right.falseCnt);

            // 'true' counter
            if (expr.res) {
                trueCnt = (trueCnt + curCnt) % MOD;
            }
            // 'false' counter
            else {
                falseCnt = (falseCnt + curCnt);
            }
        }

        return new int[]{trueCnt, falseCnt};

    }

    public static void main(String[] args) {
        try {
            new BooleanParenthesization();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private enum Operation {

        OR('|', "0,0,0|0,1,1|1,0,1|1,1,1"),
        AND('&', "0,0,0|0,1,0|1,0,0|1,1,1"),
        XOR('^', "0,0,0|0,1,1|1,0,1|1,1,0");

        final char symbol;
        final List<BooleanExpr> table;

        Operation(char symbol, String booleanTableStr) {
            this.symbol = symbol;
            this.table = parseTable(booleanTableStr);
        }

        static Operation fromChar(char ch) {

            for (Operation op : values()) {
                if (op.symbol == ch) {
                    return op;
                }
            }

            throw new IllegalArgumentException("Can't find Operation for character: '" + ch + "'");
        }

        private static List<BooleanExpr> parseTable(String booleanTableStr) {

            List<BooleanExpr> res = new ArrayList<>();

            String[] arr = booleanTableStr.split("[|]");

            for (String singleExpr : arr) {
                String[] values = singleExpr.split(",");
                res.add(BooleanExpr.create(values));
            }

            return res;
        }


    }

    private static final class BooleanExpr {
        final boolean left;
        final boolean right;
        final boolean res;

        BooleanExpr(boolean left, boolean right, boolean res) {
            this.left = left;
            this.right = right;
            this.res = res;
        }

        static BooleanExpr create(String[] str) {
            return new BooleanExpr(fromZeroOne(str[0]), fromZeroOne(str[1]), fromZeroOne(str[2]));
        }

        static boolean fromZeroOne(String val) {
            return "1".equals(val);
        }
    }

    private static final class TFCounter {
        final int trueCnt;
        final int falseCnt;

        TFCounter(int trueCnt, int falseCnt) {
            this.trueCnt = trueCnt;
            this.falseCnt = falseCnt;
        }

        static TFCounter create(char ch) {
            if (ch == 'T') {
                return new TFCounter(1, 0);
            }

            if (ch == 'F') {
                return new TFCounter(0, 1);
            }

            throw new IllegalArgumentException("Unknown true/false character: '" + ch + "'");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TFCounter tfCounter = (TFCounter) o;
            return trueCnt == tfCounter.trueCnt &&
                    falseCnt == tfCounter.falseCnt;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(trueCnt, falseCnt);
        }

        @Override
        public String toString() {
            return trueCnt + " / " + falseCnt;
        }
    }
}
