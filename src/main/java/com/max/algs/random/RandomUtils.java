package com.max.algs.random;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.max.algs.util.NumberUtils;

public final class RandomUtils {
	
	private static final Logger LOG = Logger.getLogger(RandomUtils.class);
	
	private static int maxItsCount = 0;
	
	
	public static void main( String[] args ){
		
		try {
			int totalExperimentsCount = 0;
			
			Map<Integer, Integer> occurrencyMap = new TreeMap<Integer, Integer>();
			
			for( int i = 0; i < 100000; i++, totalExperimentsCount++){
						
				
				int randomValue = RandomUtils.randomInRangeCorrect( 3, 25 );
				
				Integer count = occurrencyMap.get( randomValue );
				
				if( count == null ){
					count = Integer.valueOf(1);
				}
				else{
					++count;
				}
				occurrencyMap.put( randomValue, count );
			}
			
			LOG.info( "max its count: " + RandomUtils.getMaxItsCount() );		
			
			for( Map.Entry<Integer, Integer> entry : occurrencyMap.entrySet() ){
				LOG.info( "value: " + entry.getKey() + ",\tprobabilty: " + ( (double)entry.getValue() / totalExperimentsCount) );
			}
		}
		catch( final Exception ex ){
			LOG.error(ex);
		}
		
	}
	
	
	public static int getMaxItsCount(){
		int retValue = maxItsCount;
		maxItsCount = 0;
		return retValue;
	}
	

	public static int randomInRange( int a, int b ){
		
		final int maxOffset =  b - a;
		final int numOfBits = NumberUtils.numOfBits( maxOffset );
		
		int randOffset = 0;		
		int bitsAdded = 0; 
			
		while( bitsAdded < numOfBits ){
			int bitValue = randOneZero();
			randOffset = (randOffset << 1) | bitValue; 
			++bitsAdded;
		}
			
		return a + (randOffset % (maxOffset+1));		
	}
	
	/**
	 * 
	 * Describe an implementation of the procedure RANDOM(a, b) that only makes calls to RANDOM(0, 1). 
	 * What is the expected running time of your procedure, as a function of a and b?
	 * 
	 * time: O( c * lg(b-a) )
	 * space: O(1)
	 * 
	 */
	public static int randomInRangeCorrect( int a, int b ){
		
		final int maxOffset = b - a;
		final int numOfBits = NumberUtils.numOfBits( maxOffset );
		
		int itsCount = 0;
		int randOffset;	
		int bitsAdded;
		
		while( true ){		
		
			randOffset = 0;		
			bitsAdded = 0; 
			
			while( bitsAdded < numOfBits ){
				int bitValue = randOneZero();
				randOffset = (randOffset << 1) | bitValue; 
				++bitsAdded;
			}
			
			// 'randOffset' is in correct range
			if( randOffset <= maxOffset ){
				break;
			}
			
			++itsCount;
			
			if( itsCount > maxItsCount ){
				maxItsCount = itsCount;
			}
		}
		
		return a + randOffset;
	}
	
	
	
	
	/**
	 * 
	 * Uniformly generate random values from range [0; 1] 
	 * 
	 */
	public static int randOneZero(){
		return RAND.nextInt() & 1;		
	}
	
	private static final Random RAND = new Random();
	
	private RandomUtils(){
		super();
	}



	
	

}
