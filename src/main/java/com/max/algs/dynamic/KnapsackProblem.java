package com.max.algs.dynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import cern.colt.Arrays;

public final class KnapsackProblem {

	private static final Logger LOG = Logger.getLogger(KnapsackProblem.class);
	
	private KnapsackProblem(){
		throw new IllegalStateException( "Can't instantiate utility class '" + KnapsackProblem.class.getName()  + "'" ); 
	}
	
	/**
	 * 
	 * Knapsack problem.
	 * 
	 * 
	 * K - knapsack size
	 * N - elements count
	 * 
	 * time: O(K*N)
	 * space: O(K*N)
	 * 
	 * @param allowRepetitions, if true - knapsack doesn't allow repetitions (0-1 knapsack),
	 * if false - repetitions are allowed. 
	 */
	public static List<Integer> optimalKnapsack(int[] weightArr, int[] valueArr, int knapsackSize, 
			boolean allowRepetitions){
		
		if( weightArr == null || valueArr == null ){
			throw new IllegalArgumentException("NULL 'weightArr' or 'valueArr' array passed");
		}
		
		if( weightArr.length != valueArr.length ){
			throw new IllegalArgumentException("'weightArr'.length != 'valueArr'.length");
		}
		
		if( knapsackSize < 0 ){
			throw new IllegalArgumentException("'knapsackSize' can't be nagative: " + knapsackSize);
		}
		
		
		
		int[][] m = new int[knapsackSize+1][weightArr.length+1];
		
		for( int row = 1; row < m.length; row++ ){
			for( int col = 1; col < m[row].length; col++ ){
				
				int curK = row;
				int curValue = valueArr[col-1];
				int curWeight = weightArr[col-1];
				
				if( curWeight <= curK ){					
					if( allowRepetitions ){
						m[row][col] = Math.max( curValue + m[row-curWeight][col], m[row][col-1]);
					}
					else {					
						m[row][col] = Math.max( curValue+ m[row-curWeight][col-1], m[row][col-1]);
					}
				}
				else {
					m[row][col] = m[row][col-1];
				}
				
			}
		}
		
		displayMatrix(m);
		
		int lastRow = m.length-1;
		int lastCol = m[lastRow].length-1;		
		
		List<Integer> elems = new ArrayList<>();
		
		int row = lastRow;
		int col = lastCol;
		
		while( row > 0 && col > 0 ){
			
			if( m[row][col] != m[row][col-1]){
				elems.add(col-1);				
				row = row - weightArr[col-1];
				
				if( ! allowRepetitions ){
					--col;
				}
			}	
			else {			
				--col;
			}			
		}
		
		Collections.reverse(elems);
		
		return elems;		
	}
	
	
	
	
	private static void displayMatrix(int[][] m){
		
		for( int row = 0; row < m.length; row++ ){
			LOG.info( Arrays.toString(m[row]));
		}
		
	}
	
	
	
}
