package com.max.algs.string.matching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ShiftAndMatcher implements IStringMatcher {

	
	INST;
	
	
	private static final int MAX_PATTERN_LENGTH = 31; 
	
	@Override
	public List<Integer> validShifts(String text, String pattern) {
		
		if( text == null || pattern == null ){
			throw new IllegalArgumentException("NULL string parameter passed, text: " + text + ", pattern: " + pattern);
		}
		
		if( pattern.length() > MAX_PATTERN_LENGTH ){
			throw new IllegalArgumentException("Pattern length too big for this string matching algorithm should be less or equals to: " + 
					MAX_PATTERN_LENGTH + ", but current: " + pattern.length());
		}
		
		if( text.length() < pattern.length() ){
			return new ArrayList<>();
		}
		
		List<Integer> shifts = new ArrayList<>();
		
		if( text.length() == pattern.length() ){
			
			if( text.equals(pattern) ){
				shifts.add(0);
			}
			
			return shifts;
		}		
		
		final int matchMask = 1 << (pattern.length()-1); 
		
		Map<Character, Integer> charsMap = createCharsMap(pattern);
		
		int bitVec = 0;
		
		if( pattern.charAt(0) == text.charAt(0) ){
			bitVec = 1;
		}
		char textCh;
		int newBitVec = 0;
		int chMask;
		
		for( int i = 1; i < text.length(); i++ ){
			
			textCh = text.charAt(i);
			newBitVec = 0;
			
			if( charsMap.containsKey(textCh) ){
				newBitVec = ((bitVec << 1) | 1);
				chMask = charsMap.get( textCh );
				
				newBitVec = newBitVec & chMask;
				
				if( (newBitVec & matchMask) != 0 ){
					shifts.add( i - (pattern.length() - 1));
				}							
			}
			
			bitVec = newBitVec;
			
			
		}
		

		return shifts;
	}
	
	
	/**
	 * time: O(N)
	 * 
	 */
	private Map<Character, Integer> createCharsMap(String pattern) {			
		
		final int patternLength = pattern.length();
		
		Map<Character, Integer> charsMap = new HashMap<>();
		
		int mask = 1;
		char ch;
		Integer chMask = null;
		
		for( int i = 0; i < patternLength; i++){
			
			ch = pattern.charAt(i);			
			chMask = charsMap.get(ch);
			
			if( chMask == null ){
				charsMap.put(ch, mask);
			}
			else {
				charsMap.put(ch, chMask | mask );
			}
			
			mask <<= 1;
		}
		
		return charsMap;		

	}
	
	

}
