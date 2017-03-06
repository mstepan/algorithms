package com.max.algs.string;

import java.util.Arrays;

import org.junit.Test;


public class ZboxUtilsTest {
	
	
	
	@Test
	public void testZboxesNaive(){		
		assertArrayEquals( new int[]{0,0,0,2,0,0}, ZboxUtils.calculateZboxesNaive("abcabd") );		
		assertArrayEquals( new int[]{0,1,0,3,1,0,0,1,0,7,1,0,3,1,0,0,0}, ZboxUtils.calculateZboxesNaive("aabaabcaxaabaabcy") );		
	}
	
	@Test
	public void testZboxes(){		
		assertArrayEquals( new int[]{0,0,0,2,0,0}, ZboxUtils.calculateZboxes("abcabd") );		
		assertArrayEquals( new int[]{0,1,0,3,1,0,0,1,0,7,1,0,3,1,0,0,0}, ZboxUtils.calculateZboxes("aabaabcaxaabaabcy") );	
		
	}
	

	
	private void assertArrayEquals(int[] expected, int[] actual ){
		if( !Arrays.equals( expected, actual ) ){
			throw new AssertionError("Arrays aren't equals, expected: " + Arrays.toString(expected) + ", actual: " + Arrays.toString(actual));
		}
	}

}
