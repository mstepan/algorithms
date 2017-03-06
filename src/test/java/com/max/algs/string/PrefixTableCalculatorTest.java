package com.max.algs.string;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class PrefixTableCalculatorTest {
	
	
	
	@Test
	public void contains(){
		assertTrue( PrefixTableCalculator.contains("abd", "abcabdc") ); 		
		assertFalse( PrefixTableCalculator.contains("abd", "abcabcd") ); 
	}
	
	



}
