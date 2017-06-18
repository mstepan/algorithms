package com.max.algs.search;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class SortedMatrixSearchTest {


    @Test
    public void find() {

        int[][] matrix = {
                {1, 5, 6, 7, 9},
                {2, 4, 6, 8, 12},
                {2, 7, 9, 10, 15},
                {3, 8, 10, 12, 15},
                {8, 9, 15, 17, 22}
        };

        // search existed elements
        assertTrue(Arrays.equals(new int[]{4, 3}, SortedMatrixSearch.find(matrix, 17)));
        assertTrue(Arrays.equals(new int[]{0, 1}, SortedMatrixSearch.find(matrix, 5)));
        assertTrue(Arrays.equals(new int[]{0, 0}, SortedMatrixSearch.find(matrix, 1)));
        assertTrue(Arrays.equals(new int[]{4, 4}, SortedMatrixSearch.find(matrix, 22)));
        assertTrue(Arrays.equals(new int[]{2, 2}, SortedMatrixSearch.find(matrix, 9)));
        assertTrue(Arrays.equals(new int[]{4, 3}, SortedMatrixSearch.find(matrix, 17)));

        // search not existed elements
        assertTrue(Arrays.equals(new int[]{-1, -1}, SortedMatrixSearch.find(matrix, 25)));
        assertTrue(Arrays.equals(new int[]{-1, -1}, SortedMatrixSearch.find(matrix, 0)));
        assertTrue(Arrays.equals(new int[]{-1, -1}, SortedMatrixSearch.find(matrix, 13)));
        assertTrue(Arrays.equals(new int[]{-1, -1}, SortedMatrixSearch.find(matrix, 11)));


    }

}
