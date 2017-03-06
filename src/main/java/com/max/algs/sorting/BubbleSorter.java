package com.max.algs.sorting;

import com.max.algs.util.ArrayUtils;

public final class BubbleSorter {
	
	private BubbleSorter(){
		super();
	}
	
	public static void sort( int[] arr ){
		
		if( arr == null ){
			throw new IllegalArgumentException("'NULL' array passed");
		}
		
		// don't sort empty or one element array
		if( arr.length < 2 ){
			return;
		}		
				
		int lastPos = arr.length;
		int lastChange;
		
		while( lastPos > 1 ){
			
			lastChange = 0;
			
			for( int i = 1; i < lastPos; i++ ){					
				if( arr[i-1] > arr[i] ){
					ArrayUtils.swap(arr, i-1, i);
					lastChange = i;
				}		
			}
			
			lastPos = lastChange;
		}
	}

}
