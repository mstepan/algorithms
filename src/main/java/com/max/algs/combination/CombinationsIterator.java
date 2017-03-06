package com.max.algs.combination;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.max.algs.util.SameOnesCountIterator;

public class CombinationsIterator<E> implements Iterator<List<E>> {
	
	private final E[] arr;
	private final int combinationLength;
	private final Iterator<Integer> maskIt;
	
	public CombinationsIterator(E[] arr, int combinationLength){	
		this.arr = arr;
		this.combinationLength = combinationLength;
		this.maskIt = new SameOnesCountIterator(combinationLength, arr.length);
	}

	@Override
	public boolean hasNext() {
		return maskIt.hasNext();
	}

	@Override
	public List<E> next() {
		
		if( !hasNext() ){
			throw new NoSuchElementException();
		}
		
		
		return createFromMask(arr, maskIt.next(), combinationLength);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();	
	}	
	
	private List<E> createFromMask(E[] arr, int bitSetMask, int length){
		
		List<E> combination = new ArrayList<>(length);
		
		for( int i =0; i < arr.length && combination.size() < length; i++ ){
			if( (bitSetMask & (1<<i)) != 0 ){
				combination.add(arr[i]);
			}
		}
		
		return combination;
	}
	

}
