package com.max.algs.dynamic.formatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TextFormatter {
	
	
	private final List<String> words;
	private final int lineSize; 
	
	private final Map<Integer, FormattedText> formatCache = new HashMap<>();	
		
	
	public TextFormatter(List<String> words, int lineSize) {
		super();
		this.words = words;
		this.lineSize = lineSize;
	}


	public FormattedText formatText(){
		return optFormatRec(0);
	}	
	
	
	private FormattedText optFormatRec( int index ){		
		
		FormattedText prevFormattedText = formatCache.get(index);
		
		if( prevFormattedText != null ){
			return prevFormattedText;
		}		
		
		if( index >= words.size() ){
			return FormattedText.EMPTY;
		}		
		
		int curLength = 0;
		StringBuilder curStr = new StringBuilder(); 

		FormattedText minPartition = FormattedText.EMPTY;
		int minCost = Integer.MAX_VALUE;
				
		for( int i = index; i < words.size(); i++ ){
			
			String word = words.get(i);	
			
			if( curLength + 1 + word.length() > lineSize ){
				break;
			}
			
			if( curLength == 0 ){
				curLength = word.length();
				curStr.append( word );
			}
			else {					
				curLength += 1 + word.length();
				curStr.append(" ").append( word );
			}		
			
			FormattedText leftLines = optFormatRec( i+1 );
			
			int leftCost = leftLines.getCost();			
			int curCost = (lineSize - curLength) + leftCost; 
			
			if( curCost < minCost ){				
				minPartition = new FormattedText(curStr.toString(), leftLines.getLines(), lineSize );				
				minCost = curCost;
			}			
		}
		
		formatCache.put(index, minPartition);

		return minPartition;
	}

}
