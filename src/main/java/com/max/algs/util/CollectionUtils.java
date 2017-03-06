package com.max.algs.util;

import java.util.ArrayList;
import java.util.List;

public final class CollectionUtils {
	
	
	private CollectionUtils(){
		throw new IllegalStateException("Can't instantiate utility class");
	}
	
	public static List<Integer> generateRandomList(int length, int maxValue){
		int[] arr = ArrayUtils.generateRandomArray(length, maxValue);
		
		List<Integer> list = new ArrayList<>(arr.length);
		
		for( int i =0; i < arr.length; i++ ){
			list.add( arr[i] );
		}
		
		return list;
	}
	
	public static long calculateBitSetSizeInBytes(long minValue, long maxValue){
		
		if( minValue > maxValue ){
			minValue = minValue ^ maxValue;
			maxValue = minValue ^ maxValue;
			minValue = minValue ^ maxValue;
		}
		
		long minValuePos = minValue;
		
		if( minValue < 0 ){
			minValuePos = -minValue;
		}
		return (minValuePos + maxValue) >>> 6 << 3;
	}
	
	public static int sum(List<Integer> list){
		
		if( list == null ){
			throw new IllegalArgumentException("Can't sum values for NULL list");
		}
		
		return list.stream().parallel().reduce(0, (sum, val)-> sum+val);

	}

}
