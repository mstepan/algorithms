package com.max.algs.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SameOnesCountIterator implements Iterator<Integer> {
	
	private final int[] arrayOfOnes;	
	private final Integer maxValue;
	
	private Integer cur;
	
	public SameOnesCountIterator( int setBitsCount, int totalBitsLength ){
		arrayOfOnes = createArrayOfOnes(setBitsCount);		
		maxValue = Integer.valueOf( 1 << totalBitsLength); 		
		cur = Integer.valueOf(arrayOfOnes[arrayOfOnes.length-1]);
	}
	

	@Override
	public boolean hasNext() {
		return cur.compareTo(maxValue) < 0;
	}

	@Override
	public Integer next() {
		
		if( !hasNext() ){
			throw new NoSuchElementException();
		}
		
		
		Integer ret = cur;
		
		cur = nextWithSameBitsSet();
		
		return ret;

	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();		
	}
	
	
	private int[] createArrayOfOnes(int cnt){
		
		int mask = 0;		
		int index = 1;
		
		int[] maskArr = new int[cnt+1];
		
		while( index <= cnt ){
			mask = (mask << 1) | 1;		
			maskArr[index] = mask;
			++index;
		}

		return maskArr;
	}
	
	private Integer nextWithSameBitsSet(){
		
		int[] values = zeroFirstBlockOfOnes(cur);
		
		int nextValue = values[0];
		
		int onesBlockLength = values[1];
		nextValue = nextValue | arrayOfOnes[onesBlockLength-1];
		
		return Integer.valueOf(nextValue);
	}
	

	private int[] zeroFirstBlockOfOnes(int value){
		
		int cnt = 0;
		int mask = 1;
		
		while( (value & mask) == 0 ){
			mask <<= 1;
		}
		
		while( (value & mask) != 0 ){			
			value = value ^ mask;
			mask <<= 1;
			++cnt;
		}
		
		value = value | mask;		
		
		return new int[]{value, cnt};
	}
	

}
