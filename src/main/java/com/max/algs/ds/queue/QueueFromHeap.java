package com.max.algs.ds.queue;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Queue that use min heap as implementation.
 * Not thread safe.
 * 
 * @author Maksym Stepanenko
 *
 */
public final class QueueFromHeap {
	
	private final Queue<Entry> minHeap = new PriorityQueue<>(Entry.PRIORITY_ASC_CMP);
	
	private int priority = Integer.MIN_VALUE;
	
	/**
	 * time: O(1)
	 */
	public void add(int value){
		
		if( priority == Integer.MAX_VALUE ){
			throw new IllegalStateException("Max queue size reached"); 
		}
		
		minHeap.add(new Entry(priority, value));
		++priority;
	}
	
	/**
	 * time: O(lgN)
	 */
	public int poll(){
		Entry entry = minHeap.poll();
		
		if( minHeap.isEmpty() ){
			priority = Integer.MIN_VALUE;
		}
		
		return entry.value;
	}
	
	/**
	 * time: O(1)
	 */
	public int peek(){
		return minHeap.peek().value;
	}
	
	/**
	 * time: O(1)
	 */
	public int size(){
		return minHeap.size();
	}
	
	/**
	 * time: O(1)
	 */
	public boolean isEmpty(){
		return minHeap.isEmpty();
	}
	
	private static final class Entry {
		
		private static final Comparator<Entry> PRIORITY_ASC_CMP = new Comparator<Entry>(){

			@Override
			public int compare(Entry e1, Entry e2) {
				return Integer.compare(e1.priority, e2.priority);
			}
			
		};
		
		final int priority;
		final int value;
		
		
		Entry(int priority, int value) {
			super();
			this.priority = priority;
			this.value = value;
		}
		
		@Override
		public String toString(){
			return value + " (" + priority + ")";
		}
		
		
	}

}
