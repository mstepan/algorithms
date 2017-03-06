package com.max.algs.sorting;

import java.util.ArrayDeque;
import java.util.Deque;

public final class RadixSorter {
	
	
	private RadixSorter(){
		super();
		throw new IllegalStateException("Can't isnatntiate RadixSorter class");
	}
	
	
	/**
	 * Radix sort using LSD.
	 * 
	 * k = 10
	 * 
	 * time: O(k*N)
	 * space: O(k+N)
	 */
	public static void sort( int[] arr ){		
		
		int lastNegativeIndex = makeNegativeNumbersBoundary(arr);	
		
		if( lastNegativeIndex >= 0 ){
			changeElemetsSign(arr, 0, lastNegativeIndex);
			sortArrayRange(arr, 0, lastNegativeIndex);
			changeElemetsSign(arr, 0, lastNegativeIndex);
			reverse(arr, 0, lastNegativeIndex);
		}
		
		sortArrayRange(arr, lastNegativeIndex+1, arr.length-1);	
	}
	
	private static void reverse(int[] arr, int from, int to){
		int left = from;
		int right = to;
		
		while( left < right ){
			swap(arr, left, right);
			++left;
			--right;
		}
	}
	
	private static void changeElemetsSign(int[] arr, int from, int to){
		for( int i =from; i <=to; i++ ){
			arr[i] = -arr[i];
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void sortArrayRange( int[] arr, int from, int to ){				
		
		Deque<Integer>[] deques = new ArrayDeque[10];
		for( int i =0; i < deques.length; i++ ){
			deques[i] = new ArrayDeque<Integer>();
		}
		
		for( int digitIndex = 0; digitIndex < 11; digitIndex++){
			
			for( int i = from; i <= to; i++ ){				
				int digit = ( arr[i] / (int)Math.pow(10.0, digitIndex) ) % 10;			
				deques[digit].add( arr[i] );				
			}
			
			if( deques[0].size() == arr.length ){
				break;
			}
			
			int dequeIndex = 0;
			for( int i = from; i <= to; i++ ){
				
				while( deques[dequeIndex].isEmpty() ){
					++dequeIndex;
				}
				
				arr[i] = deques[dequeIndex].poll();				
			}			
		}		
	}
	
	
	private static int makeNegativeNumbersBoundary( int[] arr ){
		int negativeIndex = -1;		
		
		for( int i =0; i < arr.length; i++ ){
			if( arr[i] < 0 ){
				swap(arr, i, negativeIndex+1);
				++negativeIndex;
			}
		}
		
		return negativeIndex;
	}
	
	private static void swap(int[] arr, int from ,int to){
		int temp = arr[from];
		arr[from ] = arr[to];
		arr[to] = temp;
	}

}
