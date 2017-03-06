package com.max.algs.util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.List;

public final class IntegerUtils {

	private IntegerUtils() {
		throw new IllegalArgumentException("Can't instantiate utility class");
	}
	
	
	public static final Comparator<Integer> INT_DESC_CMP = new Comparator<Integer>(){
		@Override
		public int compare(Integer o1, Integer o2) {
			return -o1.compareTo(o2);
		}
		
	};
	
	
	public static List<Integer> factor(int value){
		
		List<Integer> factors = new ArrayList<>();
		int valueToFactor;
		
		if( value == Integer.MIN_VALUE ){
			factors.add(2);
			valueToFactor = value >>> 1;
		}
		else if(value == Integer.MIN_VALUE+1){
			factors.add( Math.abs(Integer.MIN_VALUE+1) );
			return factors;
		}
		else {		
			valueToFactor = Math.abs(value);
		}

		if( valueToFactor <= 1 ){
			factors.add(valueToFactor);
			return factors;
		}
		
		
		while( valueToFactor != 1){
			
			for( int prime : getPrimes(valueToFactor) ){				
				if( valueToFactor % prime == 0 ){
					factors.add(prime);
					valueToFactor = valueToFactor / prime;
					break;
				}			
			}
		}
		
		return factors;		
		
	}
	
	public static List<Integer> getPrimes(int upperBound){
		
		long length = upperBound+1;
		
		BitSet composite = new BitSet((int)length);
		
		composite.set(0);
		composite.set(1);
		
		List<Integer> primes = new ArrayList<>();
		
		int i = 2;
		
		while( i < length ){			
			
			primes.add(i);
			
			for( long mul = 2; i*mul < length; mul++ ){
				composite.set( (int)(i*mul) );
			}
			
			i = composite.nextClearBit(i+1);			
		}
		
		return primes;
		
	}

}
