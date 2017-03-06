package com.max.algs.search;

import java.util.Arrays;

public final class SearchAbsSortedArray {

	
	private SearchAbsSortedArray(){
		super();
	}
	
	
	private static int next(int[] arr, int index, boolean positive){
		
		++index;
		
		while( index < arr.length ){
			if( positive && arr[index] >= 0 ){
				return index;
			}
			
			if( !positive && arr[index] < 0 ){
				return index;
			}
			
			++index;
		}
		
		return -1;		
	}
	
	private static int prev(int[] arr, int index, boolean positive){
		
		--index;
		
		while( index >= 0 ){
			if( positive && arr[index] >= 0 ){
				return index;
			}
			
			if( !positive && arr[index] < 0 ){
				return index;
			}
			
			--index;
		}
		
		return -1;		
	}
	
	private static int[] findSumPos(int[] arr, int k ){
		
		int lo = next(arr, -1, true);
		int hi = prev(arr, arr.length, true);
		
		while( lo >=0 && hi >=0 && lo < hi ){
			
			int sum = arr[lo] + arr[hi];
			
			if( sum == k ){
				return new int[]{lo, hi};
			}
			
			if(sum < k ){
				lo = next(arr, lo, true);
			}
			else {
				hi = prev(arr, hi, true);
			}
		}
		
		
		return new int[]{-1, -1};
	}
	
	private static int[] findSumNeg(int[] arr, int k ){
		
		int lo = prev(arr, arr.length, false);
		int hi = next(arr, -1, false);
		
		while( lo >=0 && hi >=0 && hi < lo ){
			
			int sum = arr[lo] + arr[hi];
			
			if( sum == k ){
				return new int[]{ hi, lo };
			}
			
			if(sum < k ){
				lo = prev(arr, lo, false);
				
			}
			else {
				hi = next(arr, hi, false);
			}
		}
		
		
		return new int[]{-1, -1};
	}
	
	
	private static int[] findSumPosNeg(int[] arr, int k ){

		int lo = prev(arr, arr.length, false);
		int hi = prev(arr, arr.length, true);
		
		while( lo >= 0 && hi >= 0  ){
			
			int sum = arr[lo] + arr[hi];
			
			if( sum == k ){
				return new int[]{ Math.min(lo, hi), Math.max(lo, hi) };
			}
			
			if(sum < k ){
				lo = prev(arr, lo, false);
			}
			else {
				hi = prev(arr, hi, true);
			}
		}
		
		
		return new int[]{-1, -1};
	}
	
	
	public static int[] find( int[]arr, int k ){
		
		int[] pair = null;
		
		if( k >= 0 ){
			pair = findSumPos(arr, k);
		}
		else {
			pair = findSumNeg(arr, k);
		}
		
		if( ! Arrays.equals(pair, new int[]{-1, -1}) ){
			return pair;
		}
		
		
		return findSumPosNeg(arr, k);
	}
	
	
}
