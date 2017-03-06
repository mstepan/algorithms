package com.max.algs.search;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class SearchAbsSortedArrayTest {
	
	
	@Test
	public void searchNegativeValue(){	

		int[] arr = {-3, 8, 10, -12, -14, 18, 22,-25};
		
		assertArrayEquals( pair(0,7), SearchAbsSortedArray.find(arr, -28) );
		assertArrayEquals( pair(0,4), SearchAbsSortedArray.find(arr, -17) );
		assertArrayEquals( pair(0,3), SearchAbsSortedArray.find(arr, -15) );
		assertArrayEquals( pair(3,4), SearchAbsSortedArray.find(arr, -26) );
		assertArrayEquals( pair(3,7), SearchAbsSortedArray.find(arr, -37) );		
		assertArrayEquals( pair(4,7), SearchAbsSortedArray.find(arr, -39) );
		
		
		assertArrayEquals( pair(1,4), SearchAbsSortedArray.find(arr, -6) );
		
		assertArrayEquals( pair(-1,-1), SearchAbsSortedArray.find(arr, -40) );		
		assertArrayEquals( pair(-1,-1), SearchAbsSortedArray.find(arr, -150) );
	}
	
	
	@Test
	public void searchPositiveValue(){	

		int[] arr = {-3, 8, 10, -12, -14, 18, 22,-25};
		
		assertArrayEquals( pair(1,6), SearchAbsSortedArray.find(arr, 30) );
		
		assertArrayEquals( pair(1,5), SearchAbsSortedArray.find(arr, 26) );
		assertArrayEquals( pair(1,2), SearchAbsSortedArray.find(arr, 18) );
		assertArrayEquals( pair(2,6), SearchAbsSortedArray.find(arr, 32) );
		assertArrayEquals( pair(2,5), SearchAbsSortedArray.find(arr, 28) );
		assertArrayEquals( pair(5,6), SearchAbsSortedArray.find(arr, 40) );
		
		assertArrayEquals( pair(4,5), SearchAbsSortedArray.find(arr, 4) );
		
		assertArrayEquals( pair(-1,-1), SearchAbsSortedArray.find(arr, 41) );
		assertArrayEquals( pair(-1,-1), SearchAbsSortedArray.find(arr, 100) );
	}
	
	
	void assertArrayEquals(int[] expected, int[] actual ){
		assertTrue( Arrays.equals(expected, actual));
	}
	
	int[] pair(int x, int y){
		return new int[]{x, y};
	}

}
