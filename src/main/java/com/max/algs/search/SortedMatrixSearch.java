package com.max.algs.search;

import java.util.Arrays;


	
/**
* N - matrix.length*matrix[0].length
* 
* time: O(n^0.8)
* space: O(logN) - recursive calls depth
*
*/
public final class SortedMatrixSearch {

	
	private SortedMatrixSearch(){
		super();
	}
	
	
	private static final int[] NOT_FOUND = new int[]{-1,-1};
	
	public static int[] find(int[][] matrix, int value){			
		return find(matrix, value, 0, matrix.length-1, 0, matrix[0].length-1);
	}
	
	
	private static int[] find(int[][] matrix, int value, int rowLo, int rowHi, int colLo, int colHi){
		
		
		if( rowLo > rowHi || colLo > colHi ){
			return NOT_FOUND;
		}
		
		int rowMid = rowLo + (rowHi-rowLo)/2;
		int colMid = colLo + (colHi-colLo)/2;		
		
		
		if( rowLo == rowHi && colLo == colHi ){
			
			if( value == matrix[rowLo][colLo] ){
				return new int[]{rowLo, colLo};
			}
			
			return NOT_FOUND;
		}
		
		
		if( matrix[rowMid][colMid] == value ){
			return new int[]{rowMid, colMid};
		}
		
		if( matrix[rowMid][colMid] < value ){
			int[] index = find(matrix, value, rowMid+1, rowHi, colLo, colMid);
			
			if( ! Arrays.equals(NOT_FOUND, index) ){
				return index;
			}
			
			index = find(matrix, value, rowLo, rowMid, colMid+1, colHi);
			
			if( ! Arrays.equals(NOT_FOUND, index) ){
				return index;
			}
			
			index = find(matrix, value, rowMid+1, rowHi, colMid+1, colHi);
			
			if( ! Arrays.equals(NOT_FOUND, index) ){
				return index;
			}			
		}
		else {
			
			int[] index = find(matrix, value, rowMid, rowHi, colLo, colMid-1);
			
			if( ! Arrays.equals(NOT_FOUND, index) ){
				return index;
			}
			
			index = find(matrix, value, rowLo, rowMid-1, colMid, colHi);
			
			if( ! Arrays.equals(NOT_FOUND, index) ){
				return index;
			}
			
			
			index = find(matrix, value, rowLo, rowMid-1, colLo, colMid-1);
			
			if( ! Arrays.equals(NOT_FOUND, index) ){
				return index;
			}
		}
		
		
		
		return NOT_FOUND;
	}
	
}
