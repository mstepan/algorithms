package com.max.algs.codility;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
1. prefix_set

A non-empty zero-indexed array A consisting of N integers is given. The first covering prefix of array A is the smallest integer P such that 0≤P<N and such that every value that occurs in array A also occurs in sequence A[0], A[1], ..., A[P].
For example, the first covering prefix of the following 5−element array A:
A[0] = 2  A[1] = 2  A[2] = 1
A[3] = 0  A[4] = 1
is 3, because sequence [ A[0], A[1], A[2], A[3] ] equal to [2, 2, 1, 0], contains all values that occur in array A.
Write a function
class Solution { public int solution(int[] A); }
that, given a zero-indexed non-empty array A consisting of N integers, returns the first covering prefix of A.
Assume that:
N is an integer within the range [1..1,000,000];
each element of array A is an integer within the range [0..N−1].
For example, given array A such that
A[0] = 2  A[1] = 2  A[2] = 1
A[3] = 0  A[4] = 1
the function should return 3, as explained above.
Complexity:
expected worst-case time complexity is O(N);
expected worst-case space complexity is O(N), beyond input storage (not counting the storage required for input arguments).
Elements of input arrays can be modified.
  
 */
public class Alpha {
	
	private static final Logger LOG = Logger.getLogger(Alpha.class);
	
	public int solution ( int[] a ) {
		
		Set<Integer> unique = new HashSet<Integer>();
		
		int prefix = -1;
		
		for( int i = 0; i < a.length; i++ ){
			if( ! unique.contains(a[i]) ){
				unique.add( a[i] );
				prefix = i;
			}
		}
	
		return prefix;
	}
	
	private Alpha() throws Exception {		
		int[] a = {2,2,1,0,1};
		int coverPrefix =  solution( a );
		LOG.info( coverPrefix );
	}
	
	
	
	public static void main(String[] args) {
		try {
			new Alpha();
		}
		catch( Exception ex ){
			LOG.error(ex);
		}
	}
	

}
