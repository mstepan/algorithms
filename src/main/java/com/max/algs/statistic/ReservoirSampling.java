package com.max.algs.statistic;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Say you have a stream of items of large and unknown length that we can only iterate over once.
 * Create an algorithm that randomly chooses an item (or a set of items) from this stream such that each item is equally likely to be selected.
 *  
 *  
 *  http://blog.cloudera.com/blog/2013/04/hadoop-stratified-randosampling-algorithm
 *  http://en.wikipedia.org/wiki/Reservoir_sampling
 * 
 */
public final class ReservoirSampling {

	
	private static final Random RAND = ThreadLocalRandom.current();
	
	
	private ReservoirSampling() {
		super();
	}
	
	
	/**
	 * Choose 'numOfElementsToChoose' elements randomly from stream.
	 * 
	 */
	public static int[] chooseRandom( BufferedReader reader, int numOfElementsToChoose ) throws IOException { 
		
		String line = null;
		int[] randArr = new int[numOfElementsToChoose];		
		
		
		int counter = 1;
		int index = 0;
		
		while( (line = reader.readLine()) != null ){
			
			String[] numbers = line.split("\\s+");
			
			for( String numStr : numbers ){
				
				int numFromStream = Integer.valueOf(numStr);
				
				if( index < randArr.length ){
					randArr[index] = numFromStream;
					++index;
				}
				else {
					
					int prob = RAND.nextInt( counter );
					
					if( prob < numOfElementsToChoose ){						
						int randArrIndex = RAND.nextInt(randArr.length);						
						randArr[randArrIndex] = numFromStream;						
					}
				}
			
				
				++counter;				
			}			
		}
		
		return randArr; 
		
	}
	
	/**
	 * Choose one element randomly from stream.
	 * 
	 */
	public static int chooseRandom( BufferedReader reader ) throws IOException { 
				
		String line = null;
		int randValue = 0;		
		int counter = 1;
		
		while( (line = reader.readLine()) != null ){
			
			String[] numbers = line.split("\\s+");
			
			for( String numStr : numbers ){
				
				int numFromStream = Integer.valueOf(numStr);
				
				int probabilityValue = RAND.nextInt( counter );
				
				if( probabilityValue == 0 ){
					randValue = numFromStream;				
				}				
				
				++counter;				
			}			
		}
		
		return randValue; 
		
	}

}
