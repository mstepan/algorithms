package com.max.algs.regexp_engine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import com.max.algs.regengine.RegexpEngine;

public class RegexpEngineTest {
	
	
	@Ignore
	@Test
	public void regexpMatchingEmail(){		
		
		RegexpEngine regexp = new RegexpEngine("\\w+@gmail.com");	
		
		assertTrue( regexp.match("max@gmail.com") );	
		assertTrue( regexp.match("max123@gmail.com") );
		assertTrue( regexp.match("max_123_stepanenko@gmail.com") );
		assertTrue( regexp.match("stepanenko_maksi@gmail.com") );
		
		assertFalse( regexp.match("max@gmail") );	
		assertFalse( regexp.match("max@com") );
		assertFalse( regexp.match("max@gmail.") );
		assertFalse( regexp.match("max") );
		assertFalse( regexp.match("max@") );
		assertFalse( regexp.match("") );
		assertFalse( regexp.match("max@gmail.ru") );
		assertFalse( regexp.match("max@gmail.comm") );
	}
	
	@Ignore
	@Test
	public void regexpMatchingEmail2(){		
		
		RegexpEngine regexp = new RegexpEngine("\\w+@\\w+.com");	
		
		assertTrue( regexp.match("max@mail.com") );	
		assertTrue( regexp.match("max123@artima.com") );

		assertFalse( regexp.match("max@com") );
		assertFalse( regexp.match("max@gmail.") );
		assertFalse( regexp.match("max") );
		assertFalse( regexp.match("max@") );
		assertFalse( regexp.match("") );
		assertFalse( regexp.match("max@gmail.ru") );
		assertFalse( regexp.match("max@gmail.comm") );		
	}
	
	
	@Test
	public void regexpMatchingWithDigitCharClass(){
		
		RegexpEngine regexp = new RegexpEngine("a\\dc");
		assertTrue( regexp.match("a1c") );
		assertTrue( regexp.match("a0c") );
		assertTrue( regexp.match("a9c") );
		
		
		assertFalse( regexp.match("ac") );
		assertFalse( regexp.match("abc") );
		assertFalse( regexp.match("a") );
		assertFalse( regexp.match("c") );
		assertFalse( regexp.match("ab1c") );
		assertFalse( regexp.match("") );
	}
	
	
	@Ignore
	@Test
	public void regexpMatchingCharClassWithChars(){
		
		RegexpEngine regexp = new RegexpEngine("a\\wc");
		assertTrue( regexp.match("abc") );
		assertTrue( regexp.match("aec") );
		
		assertFalse( regexp.match("ac") );
		assertFalse( regexp.match("a") );
		assertFalse( regexp.match("c") );
		assertFalse( regexp.match("abdc") );
		assertFalse( regexp.match("") );
	}
	

	@Test
	public void regexpMatchingWithCharClasses(){
		
		RegexpEngine regexp = new RegexpEngine("\\w");
		assertTrue( regexp.match("a") );
		assertTrue( regexp.match("b") );
		assertTrue( regexp.match("e") );
		assertTrue( regexp.match("s") );
		
		assertFalse( regexp.match("ab") );
		assertFalse( regexp.match("") );
	}
	
	@Test
	public void regexpPerformance(){
		RegexpEngine regexp = new RegexpEngine("a?a?a?aaa");
		
		assertTrue( regexp.match("aaa") );
		assertTrue( regexp.match("aaaa") );
		assertTrue( regexp.match("aaaaa") );
		assertTrue( regexp.match("aaaaaa") );
		
		assertFalse( regexp.match("aaaaaaa") );
		
	}
	
	
	@Test
	public void regexpMatchingWithOr(){
		RegexpEngine regexp = new RegexpEngine("a|b");
		assertTrue( regexp.match("a") );
		assertTrue( regexp.match("b") );
		
		assertFalse( regexp.match("ab") );
		assertFalse( regexp.match("") );
		assertFalse( regexp.match("c") );
		assertFalse( regexp.match("ac") );
		assertFalse( regexp.match("eb") );
	}
	
	@Test
	public void regexpMatchingWithPlus(){
		
		RegexpEngine regexp = new RegexpEngine("a+b");	
		
		assertTrue( regexp.match("ab") );
		assertTrue( regexp.match("aaab") );
		assertTrue( regexp.match("aaaaaaaaaab") );
		
		assertFalse( regexp.match("b") );
		assertFalse( regexp.match("acb") );
		assertFalse( regexp.match("aaacb") );
		assertFalse( regexp.match("abb") );
		assertFalse( regexp.match("aaaabb") );
		assertFalse( regexp.match("") );
		
	}

	
	@Test
	public void regexpMatchingWithAsterix(){
		
		RegexpEngine regexp = new RegexpEngine("a*b");		
		
		assertTrue( regexp.match("ab") );
		assertTrue( regexp.match("aaaab") );
		assertTrue( regexp.match("b") );
		
	
		assertFalse( regexp.match("abb") );
		assertFalse( regexp.match("a") );
		assertFalse( regexp.match("") );		
		assertFalse( regexp.match("acbb") );
		assertFalse( regexp.match("aacb") );

	}
	
	
	@Test
	public void regexpMatchingWithDot(){
		
		RegexpEngine regexp = new RegexpEngine("a.b");		
		
		assertTrue( regexp.match("acb") );
		assertTrue( regexp.match("adb") );
		
		assertFalse( regexp.match("ab") );
		assertFalse( regexp.match("a") );
		assertFalse( regexp.match("b") );
		assertFalse( regexp.match("") );		
		assertFalse( regexp.match("acbb") );
		assertFalse( regexp.match("aacbb") );

	}
	
	@Test
	public void regexpMatchingWithTwoDots(){
		
		RegexpEngine regexp = new RegexpEngine("a.b.");		
		
		assertTrue( regexp.match("acbd") );
		assertTrue( regexp.match("adba") );
		assertTrue( regexp.match("acbb") );
		
		assertFalse( regexp.match("ab") );
		assertFalse( regexp.match("acb") );
		assertFalse( regexp.match("a") );
		assertFalse( regexp.match("b") );
		assertFalse( regexp.match("") );	

	}
	
	@Test
	public void regexpMatchingWithQuestionMarkAtEnd(){
		
		RegexpEngine regexp = new RegexpEngine("ab?");		
		
		assertTrue( regexp.match("ab") );
		assertTrue( regexp.match("a") );		
		assertFalse( regexp.match("b") );
		assertFalse( regexp.match("") );		
		assertFalse( regexp.match("aab") );

	}
	
	
	@Test
	public void regexpMatchingWithTwoQuestionMarks(){
		
		RegexpEngine regexp = new RegexpEngine("a?b?");
		
		assertTrue( regexp.match("ab") );
		assertTrue( regexp.match("a") );
		assertTrue( regexp.match("b") );
		assertTrue( regexp.match("") );
		
		assertFalse( regexp.match("aab") );

	}

	
	@Test
	public void regexpMatchingWithQuestionMark(){
		
		RegexpEngine regexp = new RegexpEngine("a?b");
		
		assertTrue( regexp.match("ab") );
		assertTrue( regexp.match("b") );
		
		assertFalse( regexp.match("aab") );

	}
	
		@Test
	public void simpleRegexpMatchingWithNumbers(){
		
		RegexpEngine regexp = new RegexpEngine("a23b");
		
		assertTrue( regexp.match("a23b") );		
		assertFalse( regexp.match("ab") );
		assertFalse( regexp.match("a23bb") );
	
	}
	
	
	@Test
	public void simpleRegexpMatching(){
		
		RegexpEngine regexp = new RegexpEngine("abba");
		
		assertTrue( regexp.match("abba") );		
		assertFalse( regexp.match("abbaa") );
		assertFalse( regexp.match("babba") );
		assertFalse( regexp.match("abbc") );		
	}
	
	@Test
	public void simpleRegexpMatchingWithSingleChar(){
		
		RegexpEngine regexp = new RegexpEngine("a");
		
		assertTrue( regexp.match("a") );		
		assertFalse( regexp.match("b") );
		assertFalse( regexp.match("aa") );	
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void incorrectRegexpWithTwoAsterix(){		
		new RegexpEngine("a**b");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void incorrectRegexpWithTwoPluses(){		
		new RegexpEngine("a++b");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void incorrectRegexpWithIncorrectorSyntax(){		
		new RegexpEngine("a|+b");
	}
	

}
