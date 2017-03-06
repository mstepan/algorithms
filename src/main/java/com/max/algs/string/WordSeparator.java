package com.max.algs.string;

import java.util.HashSet;
import java.util.Set;

public class WordSeparator {

	private final Set<String> allWords = new HashSet<>();
	
	public WordSeparator(String[] words ) {
		for( String singleWord : words ){
			allWords.add( singleWord );
		}
	}
	
	
	public String divideWords(String str){
		return divideWordsRec(0, str, "", "").trim();
	}

	
	private String divideWordsRec(int index, String str, String partialData, String res){
		
		if( index == str.length() ){
			return res;
		}
		
		String maxWithSpaces = "";
		
		for( int i = index; i < str.length(); i++ ){
			
			partialData += str.charAt(i);
			
			if( allWords.contains(partialData) ){
				
				String cur = divideWordsRec(i+1, str, "", res + " " + partialData);
				
				if( cur.length() > maxWithSpaces.length() ){
					maxWithSpaces = cur;
				}				
			}			
			
		}
		
		return maxWithSpaces;
		

	}
	
}
