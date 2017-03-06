package com.max.algs.sorting;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class SortUtilsTest {
	
	
	
	
	@Test
	public void quicksort(){
//		int[] arr = new int[]{5,3,2,7,8,1, 10, 45, 34, 23, 56, 65, 67, 68, 12,21, 25, 78, 90};
//		SortUtils.qsort(arr);
//		assertArrayEquals( new int[]{ 1, 2, 3, 5, 7, 8, 10, 12, 21, 23, 25, 34, 45, 56, 65, 67, 68, 78, 90}, arr);
	}

	
	@Test(expected = IllegalArgumentException.class )
	public void insertionSortNullArray(){	
		SortUtils.insertionSort( null, 0, 0 );	
	}
	
	
	@Test(expected = IllegalArgumentException.class )
	public void insertionSortIncorrectIndexes(){	
		SortUtils.insertionSort( new int[]{5,3,2,7,8,1}, 5, 2 );	
	}
	
	@Test
	public void insertionSortEmptyArray(){	
		SortUtils.insertionSort( new int[]{}, 0, 0 );	
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void insertionSortNegativeFromIndex(){	
		SortUtils.insertionSort( new int[]{3,2,1}, -1, 0 );	
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void insertionSortBigtoIndex(){	
		SortUtils.insertionSort( new int[]{3,2,1}, 0, 20 );	
	}
	
	@Test
	public void insertionSort(){	
		
		int[] arr = {5,3,2,7,8,1};		
		SortUtils.insertionSort(arr);			
		assertArrayEquals( arr, new int[]{1,2,3,5,7,8} );	
		
		arr = new int[]{5,1,0,4,1,5,9,8,3,1}; 		
		SortUtils.insertionSort(arr);	
		assertArrayEquals( arr, new int[]{0,1,1,1,3,4,5,5,8,9} );	
		
	}
	

}
