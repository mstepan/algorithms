package com.max.algs.search;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import com.max.algs.util.ArrayUtils;

public class UnboundedBinarySearchTest {
	
	@Test
	public void find(){
		
		assertEquals( 0, UnboundedBinarySearch.find(new int[]{5}, 5));
		assertEquals( -1, UnboundedBinarySearch.find(new int[]{5}, 7));		
		assertEquals( -1, UnboundedBinarySearch.find(new int[]{5}, -10));
		
		
		int[] arr = new int[20];
		
		for( int i =0; i < arr.length; i++ ){
			arr[i] = i;
		}		
		
		for( int i =0; i < arr.length; i++ ){
			assertEquals( i, UnboundedBinarySearch.find(arr, i) );
		}
		
		assertEquals( -1, UnboundedBinarySearch.find(arr, -5) );
		assertEquals( -1, UnboundedBinarySearch.find(arr, arr.length+1) );
		
		Random rand = new Random();
		for( int i =0; i < 10; i++ ){
			int[] randArr = ArrayUtils.generateRandomArray( rand.nextInt(1000) );			
			
			Arrays.sort(randArr);			
			
			for( int k = 0; k < randArr.length; k++ ){
				assertEquals( k, UnboundedBinarySearch.find(randArr, randArr[k]) );
			}
			
		}
		
	}

}
