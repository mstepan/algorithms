package com.max.algs.string.matching;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;


public class AllStringMatcherTest {
	
	private static final Logger LOG = Logger.getLogger(AllStringMatcherTest.class);
	
	private final IStringMatcher[] matchers =  new IStringMatcher[]{
			NaiveStringMatcher.INST,
			ZboxStringMatcher.INST,
			ShiftAndMatcher.INST
	};
	
	
	
	@Test
	public void validShifts(){	
		
		for( IStringMatcher matcher : matchers ){
			
			assertEquals( matcher.getClass().getName() + " failed", createList(0,7), matcher.validShifts("abracadabra", "abr") );	
			
			assertEquals( matcher.getClass().getName() + " failed", createList(2), matcher.validShifts("abcabd", "cab") );		
			assertEquals( matcher.getClass().getName() + " failed", createList(), matcher.validShifts("abcabd", "cad") );		
			assertEquals( matcher.getClass().getName() + " failed", createList(0,3,9,12), matcher.validShifts("aabaabcaxaabaabcy", "aab") );
			
			LOG.info( "All tests for " + matcher.getClass().getName() + " passed"  );
			
		}
	}
	
	
	private List<Integer> createList(int...arr){
		List<Integer> list = new ArrayList<>();
		
		for( int value : arr ){
			list.add( value );
		}
		
		return list;	
	}

}
