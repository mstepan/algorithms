package com.max.algs.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.junit.Test;


public class MatrixUtilsTest {
	
	private static final Logger LOG = Logger.getLogger(MatrixUtilsTest.class);

	@Test
	public void rotateSingleElementMatrix(){
		int[][] smallMatrix = { {15} };
			
		MatrixUtils.rotate(smallMatrix);
			
		assertTrue( Arrays.deepEquals(smallMatrix, new int[][]{	{15} }));
	}
	
	@Test
	public void rotateVerySmallMatrix(){
		int[][] smallMatrix = {
				{1,2},
				{3,4},
			};
			
			MatrixUtils.rotate(smallMatrix);
			
			assertTrue( Arrays.deepEquals(smallMatrix, new int[][]{
					{3,1},
					{4,2}
			}));
	}
	
	@Test
	public void rotateBigMatrix(){
		int[][] matrix = {
				{1,2,3,4,5},
				{6,7,8,9,10},
				{11,12,13,14,15},
				{16,17,18,19,20},
				{21,22,23,24,25},
			};
			
			MatrixUtils.rotate(matrix);
			
			assertTrue( Arrays.deepEquals(matrix, new int[][]{
					{21, 16, 11, 6, 1},
					{22, 17, 12, 7, 2},
					{23, 18, 13, 8, 3},
					{24, 19, 14, 9, 4},
					{25, 20, 15, 10, 5}
			}));
	}
	
	
	@Test
	public void rotateSmallMatrix(){
		int[][] smallMatrix = {
				{1,2,3},
				{4,5,6},
				{7,8,9}
			};
			
			MatrixUtils.rotate(smallMatrix);
			
			assertTrue( Arrays.deepEquals(smallMatrix, new int[][]{
					{7,4,1},
					{8,5,2},
					{9,6,3}
			}));
	}
	
	@Test
	public void rotate(){
		int[][] matrix = {
			{1,2,3,4},
			{5,6,7,8},
			{9,10,11,12},
			{13,14,15,16}
		};
		
		MatrixUtils.rotate(matrix);
		
		assertTrue( Arrays.deepEquals(matrix, new int[][]{
				{13,9,5,1},
				{14,10,6,2},
				{15,11,7,3},
				{16,12,8,4}
		}));

	}
	
	
	
	@Test
	public void bSearchRandomMatrix(){ 
		
		final int MAX_VALUE = 100;
		
		final int[][] matrix = MatrixUtils.generateRandomMatrix(111, MAX_VALUE);
		
		MatrixUtils.sort(matrix);
		
		for( int i =0; i < matrix.length; i++ ){
			for( int j =0 ; j  < matrix[i].length; j++ ){
				int key =  matrix[i][j];
				assertTrue( "Can't find element '" + key + "'" , MatrixUtils.bSearch(matrix, key) );
			}
		}

		assertFalse( MatrixUtils.bSearch(matrix, -5) );
		assertFalse( MatrixUtils.bSearch(matrix, MAX_VALUE + 5) );
		
	}
	
	
	@Test
	public void bSearch(){ 		
		
		final int[][] matrix = {
				{0, 1, 2, 5},
				{0, 1, 3, 5},
				{0, 1, 4, 8},
				{1, 6, 6, 8}
		};

		LOG.info( MatrixUtils.toString(matrix) );
		
		for( int i =0; i < matrix.length; i++ ){
			for( int j =0 ; j  < matrix[i].length; j++ ){
				int key =  matrix[i][j];
				assertTrue( "Can't find element '" + key + "'" , MatrixUtils.bSearch(matrix, key) );
			}
		}

		assertFalse( MatrixUtils.bSearch(matrix, -5) );
		assertFalse( MatrixUtils.bSearch(matrix, 7) );
		assertFalse( MatrixUtils.bSearch(matrix, 9) );		
		assertFalse( MatrixUtils.bSearch(matrix, 25) );		
	}

}
