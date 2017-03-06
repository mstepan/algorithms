package com.max.algs.search;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.max.algs.util.ArrayUtils;

public final class OrderStatisticUtils {
	
	private static final Random RAND = ThreadLocalRandom.current();
	
	
	private OrderStatisticUtils(){
		throw new IllegalStateException("Can't instantiate utility class '" + OrderStatisticUtils.class.getName() + "'");
	}
	
	
	public static int orderStat(int[] arr, int k){		
		
		if( k >= arr.length ){
			throw new IllegalArgumentException("Order statistic is too big '" + k + "', should be less or equals to '" + (arr.length-1) + "'");
		}
		
		if( k < 0 ){
			throw new IllegalArgumentException("Can't find order statistic for negative value");
		}
		
		return orderStatRec(arr, 0, arr.length-1, k);		
	}
	
	
	private static int orderStatRec(int[] arr, int from, int to, int k){
				
		int randIndex = from + RAND.nextInt(to-from+1);		
		ArrayUtils.swap(arr, randIndex, to);		
		
		int boundary = from-1;		
		int pivot = arr[to];
		
		for( int i = from ; i < to; i++ ){
			if( arr[i] < pivot ){
				ArrayUtils.swap(arr, i, boundary+1);
				++boundary;				
			}
		}
		
		int index = boundary+1;
		
		ArrayUtils.swap(arr, index, to);
		
		int pos = index-from;
		
		if( pos == k ){
			return pivot;
		}
		
		// go left
		if( pos > k ){
			return orderStatRec(arr, from, index-1, k);
		}
		
		int skippedCount = ((index-from)+1);
		
		// go right
		return orderStatRec(arr, index+1, to, k - skippedCount);
	}
	

}
