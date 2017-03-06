package com.max.algs.ds.dstree;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

public final class DigitizableInteger implements Digitazable, Serializable {
	
	private static final Logger LOG = Logger.getLogger(DigitizableInteger.class);
	
	private static AtomicInteger fullCmpCount = new AtomicInteger(0);
	
	public static final DigitizableInteger ZERO = new DigitizableInteger(0);

	private static final long serialVersionUID = 477976269404379817L;

	// [0 ... 31]
	public static final int NUM_OF_BINARY_DIGITS = 32;
	
	private final int value;
	
	
	public DigitizableInteger(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public int numOfDigits() {
		return NUM_OF_BINARY_DIGITS;
	}
	
	public static void printStat(){
		LOG.info( "Full comparisons count: " + fullCmpCount.get() );
	}

	/**
	 * Return binary digit identified by 'index' parameter.
	 * @return '0' - if specified bit is clear, '1' - otherwise
	 */
	@Override
	public int binaryDigit(int index) {
		
		if( index < 0 || index >= NUM_OF_BINARY_DIGITS ){
			throw new IllegalArgumentException("Can't obtain binary digit for index '" + index + "', should be in range [0 ; 31]");
		}
	
		
		return (value & (1 << index)) == 0 ?  0 : 1;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		fullCmpCount.incrementAndGet();
		
		if (this == obj){
			return true;				
		}
		if (obj == null || getClass() != obj.getClass()){
			return false;
		}
		DigitizableInteger other = (DigitizableInteger) obj;
		if (value != other.value){
			return false;
		}
		return true;
	}

	@Override
	public String toString(){
		return String.valueOf(value);
	}


}
