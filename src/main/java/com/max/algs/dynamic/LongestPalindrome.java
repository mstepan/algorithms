package com.max.algs.dynamic;

import java.util.Arrays;

import org.apache.log4j.Logger;

public final class LongestPalindrome {
	
	private static final Logger LOG = Logger.getLogger(LongestPalindrome.class);
	
	private static char[] reverse( char[] arr ){
		
		char[] reverted = Arrays.copyOf(arr, arr.length);
		
		int left = 0;
		int right = reverted.length-1;
		
		
		while( left < right ){
			
			swap(reverted, left, right);
			
			++left;
			--right;
		}
		
		return reverted;		
	}

	
	private static void swap(char[] arr, int left, int right) {
		char temp = arr[left];		
		arr[left] = arr[right];
		arr[right] = temp;
		
	}

	
	private static char[] longestCommonSubsequence(char[] arr1, char[] arr2){
		
		int[][] sol = new int[arr2.length+1][arr1.length+1];
		
		// set first row to '0'
		for( int col = 0; col < sol[0].length; col++ ){
			sol[0][col] = 0;
		}
		
		// set first column to '0'
		for( int row = 1; row < sol.length; row++ ){
			sol[row][0] = 0;
		}
		
		
		for( int row = 1; row < sol.length; row++ ){
			for( int col = 1; col < sol[row].length; col++ ){
				
				int curSol = Math.max(sol[row][col-1], sol[row-1][col]);
				
				if( arr2[row-1] == arr1[col-1] ){
					curSol = Math.max(curSol, 1+sol[row-1][col-1]);
				}
				
				sol[row][col] = curSol;
			}
		}
		
		int lastRow = sol.length-1;
		int lastCol = sol[lastRow].length-1;
		LOG.info( "Longest palindromic subsequence: " + sol[lastRow][lastCol] );
		
		// reconstructFloatFromRawBits solution
		return buildSolution(sol, arr1, arr2);
	}
	
	private static char[] buildSolution(int[][] sol, char[] arr1, char[] arr2){
		
		int row = sol.length-1;
		int col = sol[row].length-1;
		
		final int resSize = sol[row][col];
		
		char[] res = new char[resSize];
		int index = resSize-1;
		
		while( row !=0 && col != 0 ){
			
			if( sol[row-1][col] == sol[row][col] ){
				--row;
			}
			else if( sol[row][col-1] == sol[row][col] ){
				--col;
			}
			else {
				
				
				res[index] = arr1[col-1];
				--index;
				
				--row;
				--col;
			}
		}
		
		return res;
	}
	
	private LongestPalindrome() throws Exception {	

		char[] arr = {'A','C','G','T','G','T','C','A','A','A','A','T','C','G'};
		
		LOG.info( Arrays.toString(arr) );
				
		char[] reverted = reverse(arr);
		
		LOG.info( Arrays.toString(reverted) );		
		
		char[] seq = longestCommonSubsequence(arr, reverted);
		
		LOG.info( "longest palindrome: " + Arrays.toString(seq) );
		
	}
	


	public static void main(String[] args) {
		try {			
			new LongestPalindrome();
		}
		catch( Exception ex ){
			LOG.error(ex);
		}
	}


}
