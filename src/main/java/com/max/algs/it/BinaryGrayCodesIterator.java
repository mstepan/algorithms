package com.max.algs.it;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Generate binary reflected Gray codes.
 * 
 * 
 * example: n = 3
 * 
 * 000
 * 001
 * 011
 * 010
 * 110
 * 111
 * 101
 * 100
 * 
 * 
 */
public class BinaryGrayCodesIterator implements Iterator<Integer> {

	private int value = 0;
	
	private int generatedValuesCount;
	private final int totalElemsCount;
	
	
	
	public BinaryGrayCodesIterator(int elems) {
		super();
		this.totalElemsCount = (int)Math.pow(2.0, elems);
	}

	@Override
	public boolean hasNext() {
		return generatedValuesCount < totalElemsCount;
	}

	@Override
	public Integer next() {
		
		if( !hasNext() ){
			throw new NoSuchElementException();
		}
			
		int res = value;
		++generatedValuesCount;		
		
		generateNext();

		return res;
	}
	
	private void generateNext(){
		
		if( value == 0 ){
			value = 1;
			return;
		}
		
		int mask = 1;
		
		for(int i = 0; i < 32; i++, mask <<= 1 ) {
			
			if( (generatedValuesCount & mask) != 0 ){
				
				if( (value & mask) > 0){ 
					// clear bit
					value &= ~mask;
				}
				else {
					// set bit
					value |= mask;
				}
				
				break;
			}
		}
		
		
		
	}
	
	

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
