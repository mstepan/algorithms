package com.max.algs.dynamic;

import com.max.algs.dynamic.MatrixChainMultiplication.MulSolution;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatrixChainMultiplicationTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructMatrixChainMultiplicationWithNullValue() {
        new MatrixChainMultiplication(null);
    }

    @Test
    public void constructMatrixChainMultiplicationWithEmptyMatrixes() {
        assertEquals(0L, new MatrixChainMultiplication(new MatrixChainMultiplication.MatrixDimension[]{}).optimalCost());
    }


    @Test
    public void optimalCost() {
        MatrixChainMultiplication.MatrixDimension[] a = {
                new MatrixChainMultiplication.MatrixDimension("A", 50, 20),
                new MatrixChainMultiplication.MatrixDimension("B", 20, 1),
                new MatrixChainMultiplication.MatrixDimension("C", 1, 10),
                new MatrixChainMultiplication.MatrixDimension("D", 10, 100)

        };


        long optimalCost = new MatrixChainMultiplication(a).optimalCost();

        assertEquals(7000L, optimalCost);
    }

    @Test
    public void optimalSolution() {
        MatrixChainMultiplication.MatrixDimension[] a = {
                new MatrixChainMultiplication.MatrixDimension("A", 50, 20),
                new MatrixChainMultiplication.MatrixDimension("B", 20, 1),
                new MatrixChainMultiplication.MatrixDimension("C", 1, 10),
                new MatrixChainMultiplication.MatrixDimension("D", 10, 100)

        };


        MulSolution solution = new MatrixChainMultiplication(a).optimalSolution();

        assertEquals("(A*B)*(C*D)", String.valueOf(solution));

    }

}
