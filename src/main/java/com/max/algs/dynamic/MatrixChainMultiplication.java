package com.max.algs.dynamic;


public final class MatrixChainMultiplication {

    private final MatrixDimension[] matrixes;
    private final int matrixLength;

    public MatrixChainMultiplication(MatrixDimension[] matrixes) {


        if (matrixes == null) {
            throw new IllegalArgumentException("NULL 'matrixes' parameter passed");
        }

        this.matrixes = matrixes;
        this.matrixLength = matrixes.length;
    }


    public long optimalCost() {

        if (matrixLength == 0) {
            return 0L;
        }

        long[][] opt = new long[matrixLength][matrixLength];

        for (int i = matrixLength - 1; i >= 0; i--) {

            opt[i][i] = 0;

            for (int j = i + 1; j < matrixLength; j++) {
                opt[i][j] = Long.MAX_VALUE;

                for (int k = i; k < j; k++) {

                    long divCost = opt[i][k] + opt[k + 1][j] +
                            (matrixes[i].first * matrixes[k].second * matrixes[j].second);

                    if (divCost < opt[i][j]) {
                        opt[i][j] = divCost;
                    }
                }
            }
        }


        return opt[0][matrixLength - 1];
    }


    public MulSolution optimalSolution() {

        if (matrixLength == 0) {
            return new TerminalMulSolution(null);
        }

        int[][] sol = new int[matrixLength][matrixLength];
        long[][] opt = new long[matrixLength][matrixLength];

        for (int i = matrixLength - 1; i >= 0; i--) {

            opt[i][i] = 0;

            for (int j = i + 1; j < matrixLength; j++) {
                opt[i][j] = Long.MAX_VALUE;

                for (int k = i; k < j; k++) {

                    long divCost = opt[i][k] + opt[k + 1][j] +
                            (matrixes[i].first * matrixes[k].second * matrixes[j].second);

                    if (divCost < opt[i][j]) {
                        opt[i][j] = divCost;
                        sol[i][j] = k;
                    }
                }
            }
        }

        return buildSolutionRec(0, sol.length - 1, sol, matrixes);
    }


    private MulSolution buildSolutionRec(int from, int to, int[][] sol, MatrixDimension[] matrixes) {

        int count = (to - from + 1);

        if (count == 1) {
            return new TerminalMulSolution(matrixes[from]);
        }

        int k = sol[from][to];
        return new ComplexMulSolution(buildSolutionRec(from, k, sol, matrixes), buildSolutionRec(k + 1, to, sol, matrixes));
    }


    public interface MulSolution {
    }

    private static final class ComplexMulSolution implements MulSolution {


        private final MulSolution left;
        private final MulSolution right;
        ComplexMulSolution(MulSolution left, MulSolution right) {
            super();
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {

            StringBuilder buf = new StringBuilder();

            if (left instanceof ComplexMulSolution) {
                buf.append("(").append(left).append(")");
            }
            else {
                buf.append(left);
            }

            buf.append("*");

            if (right instanceof ComplexMulSolution) {
                buf.append("(").append(right).append(")");
            }
            else {
                buf.append(right);
            }


            return buf.toString();
        }
    }

    private static final class TerminalMulSolution implements MulSolution {

        private final MatrixDimension dim;


        TerminalMulSolution(MatrixDimension dim) {
            super();
            this.dim = dim;
        }


        @Override
        public String toString() {
            return dim == null ? "" : String.valueOf(dim);
        }
    }


    public static final class MatrixDimension {

        private final String label;
        private final int first;
        private final int second;

        public MatrixDimension(String label, int first, int second) {
            super();
            this.label = label;
            this.first = first;
            this.second = second;
        }

        @Override
        public String toString() {
            return label;

        }
    }

}
