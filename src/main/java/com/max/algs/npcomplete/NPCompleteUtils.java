package com.max.algs.npcomplete;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;
import java.util.Set;

public final class NPCompleteUtils {
	
	private NPCompleteUtils(){
		super();
	}
	
	
	public static int optimalSubsetSum( int[] arr, int value ){
		
		if( arr == null ){
			throw new IllegalArgumentException("NULL 'arr' passed");
		}
		
		Set<Integer> sums = new LinkedHashSet<>();
		sums.add(0);
		
		int minDifference = Integer.MAX_VALUE;
		int mostOptimalValue = 0;
		
		for( int i = 0; i < arr.length; i++ ){
			
			Set<Integer> newSums = new LinkedHashSet<>( sums );
			
			for( int prevSum : sums ){
				
				int curSum = prevSum + arr[i];
				int curDiff = Math.abs(curSum - value); 
				
				if( curDiff == 0 ){
					return curSum;
				}		
				
				if( curDiff < minDifference ){
					minDifference = curDiff;
					mostOptimalValue = curSum;
				}				
				
				newSums.add( curSum );
			}
			
			sums = newSums;
		}
		
		return mostOptimalValue;
	}
	
	
	private static final Comparator<Integer> REVERSE_INT_CMP = new Comparator<Integer>(){
		@Override
		public int compare(Integer o1, Integer o2) {
			return o2.compareTo(o1);
		}			
	};
	
	public static boolean canPartitionKarmarkarKarp( int[] baseArr ){		
		// create max heap
		PriorityQueue<Integer> heap = new PriorityQueue<Integer>(baseArr.length, REVERSE_INT_CMP);  		
		for( int value : baseArr ){
			heap.add( value );
		}		
		while( heap.size() > 1 ){
			int val1 = heap.poll();
			int val2 = heap.poll();
			
			heap.add( val1 - val2 );
		}		
		return heap.poll() == 0;		
	}
	
	public static  boolean canPartitionGreedy( int[] baseArr ){
		
		int[] arr = Arrays.copyOf(baseArr, baseArr.length);
				
		Arrays.sort(arr);
		
		int set1 = 0;
		int set2 = 0;
		
		for( int i = arr.length-1; i >= 0; i-- ){
			
			if( set1 < set2 ){
				set1 += arr[i];
			}
			else {
				set2 += arr[i];
			}
		}
		
		return set1 == set2;
	}
	
	public static boolean canPartitionDynamic( int[] arr ){
		
		int sum = 0;
		
		for( int value : arr ){
			sum += value;
		}
		
		int halfSum = sum/2;
		
		boolean[][] opt = new boolean[halfSum+1][arr.length+1];
		opt[0][0] = true;
		
		for( int col = 1; col < opt[0].length; col++ ){
			opt[0][col] = true;
		}
		
		for( int row = 1; row < opt.length; row++ ){
			opt[row][0] = false;
		}
		
		for( int row = 1; row < opt.length; row++ ){
			for( int col = 1; col < opt[row].length; col++ ){				
				int colIndex = row-arr[col-1]; 				
				opt[row][col] = opt[row][col-1] || (colIndex >= 0 ? opt[colIndex][col-1] : false);				
			}
		}
		
		// even array length
		if( (arr.length & 1) == 0){
			return opt[halfSum][arr.length];
		}
		
		
		return opt[halfSum][arr.length];
		
	}

}
