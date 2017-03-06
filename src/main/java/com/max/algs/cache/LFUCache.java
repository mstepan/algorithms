package com.max.algs.cache;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cern.colt.Arrays;

/**
 * Least frequently used cache replacement algorithm.
 * Not thread safe.
 * 
 */
public class LFUCache<T> implements Collection<T> {
	
	
	private final Node<T>[] minHeap;
	private final Map<T, Integer> positionMap = new HashMap<>();
	
	private int size;

	@SuppressWarnings("unchecked")
	public LFUCache(int capacity) {
		checkArgument(capacity > 0, "'0' or negative capacity passed: %s", capacity);
		minHeap = (Node<T>[])new Node[capacity];
	}
	
	/**
	 * K - cache capacity
	 * time: O(lgK)
	 */
	@Override
	public boolean add(T value) {
		
		Integer index = positionMap.get(value);
		
		// new element
		if( index == null ){
			
			// enought space to add new element
			if( size < minHeap.length ){
				Node<T> newNode = new Node<>(value);
				minHeap[size] = newNode;
				
				int curIndex = size;
				int parentIndex = heapParent(curIndex);
				
				while( curIndex != 0 && minHeap[parentIndex].freq > newNode.freq  ){					
					swapInMinHeap(curIndex, parentIndex);
					
					curIndex = parentIndex;
					parentIndex = heapParent(curIndex);					
				}
				
				positionMap.put(value, curIndex);	
				++size;
			}	
			
			/** 
			 * - evict element with smallest frequency (this element should be on top of min-heap)
			 * - add new one instead
			 * optimization note: reuse existing Node<T> object (overwrite value and frequency)
			 */
			else {
				T prevValue = minHeap[0].value;
				
				minHeap[0].value = value;
				minHeap[0].freq = 1;
				
				positionMap.remove(prevValue);
				positionMap.put(value, 0);				
			}			
		}
		// already in cache
		else {
			minHeap[index].freq += 1;			
			fixDown(index);		
		}
		
		return true;
	}
	
	
	private void fixDown(int curIndex){
		
		int minElementIndex = curIndex;
		int child1 = heapChild1(curIndex);
		int child2 = heapChild2(curIndex);
		
		while( true ){
			
			if( child1 < minHeap.length && minHeap[child1].freq < minHeap[minElementIndex].freq ){
				minElementIndex = child1;
			}
			
			if( child2 < minHeap.length && minHeap[child2].freq < minHeap[minElementIndex].freq ){
				minElementIndex = child2;
			}

			if( minElementIndex == curIndex ){
				break;
			}
			
			swapInMinHeap(curIndex, minElementIndex);
			curIndex = minElementIndex;
			
			child1 = heapChild1(curIndex);
			child2 = heapChild2(curIndex);				
		}				
	}
	
	@Override
	public String toString(){
		StringBuilder buf = new StringBuilder();
		
		buf.append("min_heap: ").append( Arrays.toString(minHeap)).
			append(", position_map: ").append(positionMap);
		
		return buf.toString();
	}
	
	private void swapInMinHeap(int from, int to){
		Node<T> temp = minHeap[from];
		minHeap[from] = minHeap[to];
		minHeap[to] = temp;
		
		positionMap.put(minHeap[from].value, from);
		positionMap.put(minHeap[to].value, to);
	}
	
	private int heapParent(int index){
		return index>>1;
	}
	
	private int heapChild1(int index){
		return (index << 1) + 1;
	}
	
	private int heapChild2(int index){
		return (index << 1) + 2;
	}
	
	/**
	 * K - cache capacity
	 * time: O(lgK)
	 */
	@Override
	public boolean contains(Object value) {
		
		Integer index = positionMap.get(value);
		
		if( index == null ){
			return false;
		}
		
		minHeap[index].freq += 1;
		
		fixDown(index);
		
		return true;
	}
	
	@Override
	public int size(){
		return size;
	}
	
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Iterator<T> iterator() {
		return positionMap.keySet().iterator();
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> otherCol) {
		
		checkNotNull(otherCol);
		
		for( Object value : otherCol ){
			if( ! contains(value) ){
				return false;
			}
		}
		
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addAll(Collection<? extends T> otherCol) {
		
		checkNotNull(otherCol);
		
		for( Object value : otherCol ){
			add((T)value);
		}
		
		return otherCol.isEmpty() ? false : true;
	}

	@Override
	public boolean removeAll(Collection<?> otherCol) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void checkNotNull(Collection<?> otherCol){
		checkArgument(otherCol != null, "null 'otherCol' passed");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {		
		for( int i =0; i < size; i++) {
			minHeap[i] = null;
		}
		positionMap.clear();		
		size = 0;		
	}
	
	private static final class Node<U> {
		
		U value;
		int freq;
		
		Node(U value, int freq) {
			this.value = value;
			this.freq = freq;
		}
		
		Node(U value) {
			this(value, 1);
		}
		
		@Override
		public String toString(){
			return value + ": " + freq;
		}
		
	}



}
