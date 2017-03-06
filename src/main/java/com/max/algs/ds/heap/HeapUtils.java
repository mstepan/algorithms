package com.max.algs.ds.heap;

import static com.google.common.base.Preconditions.checkArgument;

public final class HeapUtils {
	
	
	
	private static final class MutableInt {
		
		private int value;
		
		void inc(){
			value += 1;
		}
		
		@Override
		public String toString(){
			return String.valueOf(value);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + value;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MutableInt other = (MutableInt) obj;
			if (value != other.value)
				return false;
			return true;
		}
		
		
		
		
	}

	/**
	 * K - expectedCount
	 * N - arr.length
	 * time: O(K)
	 * space: O(K)
	 */
	public static boolean checkGreater(int[] arr, int value, int expectedCount){
		
		checkArgument( arr != null, "Null 'arr' parameter passed");
		checkArgument( expectedCount >= 0, "'expectedCount' is negative, should be positive or zero: expectedCount = %s", expectedCount);
		
		if( expectedCount == 0 ){
			return true;
		}
		
		if( expectedCount >= arr.length ){
			return false;
		}
		
		MutableInt actualGreaterCount = new MutableInt();
		checkRec(arr, value, expectedCount, 0, actualGreaterCount);
		return actualGreaterCount.value >= expectedCount;
	}

	
	private static void checkRec(int[] arr, int value, int k, int index, MutableInt actualGreaterCount) {
		
		if( actualGreaterCount.value >= k || index >= arr.length || arr[index] <= value) {
			return;
		}
		
		actualGreaterCount.inc();	
		
		// check first child pass
		checkRec(arr, value, k, 2*index+1, actualGreaterCount);
		
		// check second child
		checkRec(arr, value, k, 2*index+2, actualGreaterCount);		
	}
	
	private HeapUtils(){}

}
