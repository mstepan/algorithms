package com.max.algs.ds;

import org.apache.log4j.Logger;


/**
 * space: O(N)
 */
public final class LazyInitArray {
	
	private static final Logger LOG = Logger.getLogger(LazyInitArray.class);
	
	private final boolean[] arr;
	private final int[] ptrArr;
	
	private int initSize = 0;
	private final int[] initArr;
	
	
	public LazyInitArray(int capacity) {		
		if( capacity <=0 ){
			throw new IllegalArgumentException("Negative or zero capacity passed: " + capacity); 
		}		
		arr = new boolean[capacity];
		ptrArr = new int[capacity];
		initArr = new int[capacity];		
	}
	
	/**
	 * time: O(1)
	 */
	public void set(int index, boolean value){
		
		checkRange( index );
		
		if( isInitialised(index) ){
			arr[index] = value;
		}
		else {
			// not initialized yet			
			arr[index] = value;
			ptrArr[index] = initSize;
			initArr[initSize] = index;
			++initSize;
		}		
		
	}	
	
	/**
	 * time: O(1)
	 */
	public boolean get(int index){ 
		
		checkRange( index );
		
		if( isInitialised(index) ){
			return arr[index];
		}
		
		return false;
	}
	
	/**
	 * time: O(1)
	 */
	private boolean isInitialised( int index ){
		
		assert index >= 0 && index < arr.length;
		
		if( ptrArr[index] >= initSize ){
			return false;
		}
		
		int initIndex = ptrArr[index];		
		
		return index == initArr[initIndex];
		
	}
	

	private void checkRange(int index){
		if( index < 0 || index > arr.length ){
			throw new IllegalArgumentException("Incorrect index passed, index = " + index + ", should be in range[0;" + (arr.length-1) + "]");
		}
	}
	
	
	
	public static void main(String[] args){ 		
		LazyInitArray arr = new LazyInitArray(10);		
		arr.set(5, true);
		arr.set(7, false);		
		LOG.info( "Main done" );
	}

}
