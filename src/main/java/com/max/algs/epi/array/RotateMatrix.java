package com.max.algs.epi.array;

import org.apache.log4j.Logger;

public class RotateMatrix {

    private static final Logger LOG = Logger.getLogger(RotateMatrix.class);

    private RotateMatrix() throws Exception {

        int n = 4;

        int[][] m = new int[n][n];

        for (int row = 0, elem = 1; row < n; ++row) {
            for (int col = 0; col < n; ++col, ++elem) {
                m[row][col] = elem;
            }
        }

        MatrixWrapper mWrapper1 = new MatrixWrapper(m);
        System.out.println(mWrapper1);

        MatrixWrapper mWrapper2 = mWrapper1.rotate();
        System.out.println(mWrapper2);

        MatrixWrapper mWrapper3 = mWrapper2.rotate();
        System.out.println(mWrapper3);

        MatrixWrapper mWrapper4 = mWrapper3.rotate();
        System.out.println(mWrapper4);

        MatrixWrapper mWrapper5 = mWrapper4.rotate();
        System.out.println(mWrapper5);

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new RotateMatrix();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    class MatrixWrapperRotated extends MatrixWrapper {

        final MatrixWrapper base;

        MatrixWrapperRotated(MatrixWrapper base) {
            super(base.m);
            this.base = base;
        }

        @Override
        int getValue(int row, int col) {
            return base.getValue(m.length - 1 - col, row);
        }
    }

    class MatrixWrapper {

        final int[][] m;

        public MatrixWrapper(int[][] m) {
            this.m = m;
        }

        int getValue(int row, int col) {
            return m[row][col];
        }

        void setValue(int value, int row, int col) {
            m[row][col] = value;
        }

        int rows() {
            return m.length;
        }

        int cols(int row) {
            return m[row].length;
        }

        /**
         * Rotate matrix 90 degrees clock-wise.
         * time: O(1)
         * space: O(1)
         */
        MatrixWrapper rotate() {
            return new MatrixWrapperRotated(this);
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();

            for (int row = 0; row < rows(); ++row) {
                for (int col = 0; col < cols(row); col++) {
                    if (col > 0) {
                        buf.append(", ");
                    }
                    buf.append(getValue(row, col));
                }

                buf.append(System.getProperty("line.separator"));
            }


            return buf.toString();
        }
    }

}
