package com.max.algs.np_complete;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.max.algs.npcomplete.NPCompleteUtils;


public class NPCompleteUtilsTest {
	
	
	@Test
	public void optimalSubsetSum(){		
		assertEquals( 0, NPCompleteUtils.optimalSubsetSum( new int[]{-7, -3, -2, 5, 8}, 0 ) );	
		assertEquals( -10, NPCompleteUtils.optimalSubsetSum( new int[]{-7, -3, -2, 5, 8}, -10 ) );		
		assertEquals( 10, NPCompleteUtils.optimalSubsetSum( new int[]{-7, -3, -2, 5, 8}, 10 ) );
		assertEquals( 8, NPCompleteUtils.optimalSubsetSum( new int[]{-7, -3, -2, 5, 8}, 9 ) );		
		assertEquals( 3, NPCompleteUtils.optimalSubsetSum( new int[]{-7, -3, -2, 5, 8}, 3 ) );
		
	}

}
