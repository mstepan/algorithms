package com.max.algs.string;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;
import org.junit.Test;


public class StringUtilsTest {
	
	private static final Logger LOG = Logger.getLogger(StringUtilsTest.class);
	
	@Test
	public void isShuffle(){
		String x = "chocolate";
		String y = "chips";
		
		assertTrue( StringUtils.isShuffle(x, y, "cchocohilaptes") );		
		assertFalse( StringUtils.isShuffle(x, y, "chocochilatspe") );
		
		
		for( int i = 0; i < 10; i++ ){
			String str1 = "some custom big string";
			String str2 = "what you see is what you get";
			
			String shuffledStr = StringUtils.shuffle(str1, str2);
			assertTrue( StringUtils.isShuffle(str1, str2, shuffledStr) );
		}
		
	}
	
	
	@Test
	public void isCircularRandomStrings(){
		
		Random rand = ThreadLocalRandom.current();
			
		for( int itCount = 0; itCount < 1000; itCount++ ){	
			
			int strLength = 5 + rand.nextInt(100);
			int difCharsCount = 2 + rand.nextInt(10);			
			StringBuilder buf = new StringBuilder(strLength);
			
			for( int i = 0; i < strLength; i++ ){
				char randCh = (char)('a' + rand.nextInt( difCharsCount ));
				buf.append( randCh );
			}
	
			String a = buf.toString();			
			String b = StringUtils.rotateString(a, 1 + rand.nextInt(a.length() - 1) );
			
			try{
				assertTrue( StringUtils.isCircular(a, b) );
			}
			catch( AssertionError ex ){
				LOG.info( "a: " + a );
				LOG.info( "b: " + b );
				throw ex;
			}
		}		
		
	}
	
	@Test
	public void isCircular(){

		
		assertTrue( StringUtils.isCircular("dddcaaecbd", "aecbddddca") );
		
		assertTrue( StringUtils.isCircular("bdcab", "cabbd") );
		
		assertTrue( StringUtils.isCircular("abababc", "ababcab") );
		
		assertTrue( StringUtils.isCircular("", "") );	
		assertTrue( StringUtils.isCircular("aaa", "aaa") );
		assertTrue( StringUtils.isCircular("abac", "baca") );		
		assertTrue( StringUtils.isCircular("abcdef", "defabc") );
		
		assertFalse( StringUtils.isCircular("", "defabc") );		
		assertFalse( StringUtils.isCircular("abac", "bacaa") );

	}

	
	@Test
	public void rotateString(){
		assertEquals( "bca", StringUtils.rotateString("abc", 1) );		
		assertEquals( "defabc", StringUtils.rotateString("abcdef", 3) );		
	}

    @Test
    public void longestStringWithoutRepNullRandom(){
        for( int i =0; i < 10; i++){
            String str = StringUtils.createASCIIString(100);
            String longest1 = StringUtils.longestStringWithoutRepBruteforce(str);
            String longest2 = StringUtils.longestStringWithoutRep(str);    
            assertEquals( "longest1 != longest2, longest1 = " + longest1 + ", longest2 = " + longest2, longest1, longest2 );
        }
    }


    @Test(expected = IllegalArgumentException.class)
    public void longestStringWithoutRepNullStr(){
        StringUtils.longestStringWithoutRep( null );
    }


    @Test
    public void longestStringWithoutRep(){
    	assertEquals( "bcade", StringUtils.longestStringWithoutRep("dabcade") );
    	assertEquals( "bcade", StringUtils.longestStringWithoutRep("dbcade") );
    	assertEquals( "fbcade", StringUtils.longestStringWithoutRep("fbcade") );
    	assertEquals( "bcade", StringUtils.longestStringWithoutRep("dabcaded") );
    	assertEquals( "a", StringUtils.longestStringWithoutRep("aaaaa") );
    	assertEquals( "", StringUtils.longestStringWithoutRep("") );
    }

	
	@Test
	public void firstNonRepeatedChar(){
		assertEquals( 's', StringUtils.firstNonRepeatedChar("tslmtmal") );
	}
	
	
	@Test
	public void isAscciString(){
		assertTrue( StringUtils.isAscciString("ascii string") );
		assertFalse( StringUtils.isAscciString("ascii \u0100 string") );
	}

}
