package com.max.algs.dynamic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * A contiguous subsequence of a list S is a subsequence made up of consecutive elements of S. For instance, if S is
 * 5, 15, −30, 10, −5, 40, 10, then 15, −30, 10 is a contiguous subsequence but 5, 15, 40 is not. 
 * Give a linear-time algorithm for the following task:
 * Input: A list of numbers, a1, a2, . . . , an.
 * Output: The contiguous subsequence of maximum sum (a subsequence of length zero has sum zero).
 * For the preceding example, the answer would be 10, −5, 40, 10, with a sum of 55.
 * 
 * Time: O(N)
 * Space: O(1)
 * 
 * @author Maksym Stepanenko
 *
 */
public final class MaxContiguousSum {
	
	
	private static final Logger LOG = Logger.getLogger(MaxContiguousSum.class);


	public static List<Integer> maxContiguousSum(int[] arr){
		if( arr == null ){
			throw new IllegalArgumentException("NULL array passed");
		}
		
		int maxIndex = -1;
		int maxSoFar = 0;
		int maxCur = 0;
		
		for( int i =0; i < arr.length; i++ ){
			maxCur = Math.max(0, maxCur+arr[i]);
			
			if( maxCur > maxSoFar ){
				maxSoFar = maxCur;
				maxIndex= i;
			}
		}
		
		// all elements in array are negative or zero, find largest one
		if( maxIndex < 0 ){
			List<Integer> res = new ArrayList<Integer>();
			res.add( Arrays.stream(arr).parallel().max().getAsInt() );
			return res;
		}
		
		return reconstructSolution(arr, maxIndex, maxSoFar);
	}

	
	private static List<Integer> reconstructSolution(int[] arr, int index, int value){
		assert arr != null;
		assert index >= 0 && index < arr.length;
		assert value >= 0;
		
		List<Integer> res = new ArrayList<>();
		
		int i = index;
		int collectedValue = 0;
		
		while( i >= 0 && collectedValue < value ){
			res.add(arr[i]);
			collectedValue += arr[i];
			--i;
		}
		
		return res;
	}
	
	private MaxContiguousSum() throws Exception {	
		
		int[] arr = new int[]{ -2, 11, -4, 13, -5, 2 };
		List<Integer> res = maxContiguousSum(arr);

		LOG.info( res );
		System.out.printf( "sum = %d%n", res.stream().parallel().mapToInt(value->value).sum());
	
	}
	
	public static void main(String[] args) {
		try {			
			new MaxContiguousSum();
		}
		catch( Exception ex ){
			LOG.error(ex);
		}
	}

	

}
