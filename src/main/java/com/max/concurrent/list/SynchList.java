package com.max.concurrent.list;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Fully synchronized coarse grained single linked list.
 * 
 * @author Maksym Stepanenko
 *
 */
public class SynchList {
	
	
	private final Lock mutex = new ReentrantLock();
	
	private final Node tail = new Node(Integer.MAX_VALUE);
	private final Node head = new Node(Integer.MIN_VALUE, tail);
	
	
	public boolean add(int value){
		
		mutex.lock();
		try{
			Node prev = head;
			Node cur = prev.next;
			
			while( cur.value < value ){
				prev = cur;
				cur = cur.next;
			}
			
			if( cur.value == value ){
				return false;
			}
			
			Node newNode = new Node(value);		
			newNode.next = cur;
			prev.next = newNode;
			
			return true;
			
		}
		finally {
			mutex.unlock();
		}
	}
	
	
	public boolean remove(int value){
		mutex.lock();
		try{
			Node prev = head;
			Node cur = prev.next;
			
			while( cur.value < value ){
				prev = cur;
				cur = cur.next;
			}
			
			if( cur.value == value ){
				prev.next = cur.next;
				cur.next = null;
				return true;
			}		
			
		}
		finally {
			mutex.unlock();
		}
		return false;
	}
	
	
	public boolean contains(int value){
		mutex.lock();
		try{
			Node prev = head;
			Node cur = prev.next;
			
			while( cur.value < value ){
				prev = cur;
				cur = cur.next;
			}
			
			if( cur.value == value ){
				return true;
			}
			
		}
		finally {
			mutex.unlock();
		}
		return false;
	}
	
	
	private static final class Node {
		
		final int value;
		Node next;
		
		Node(int value, Node next) {
			super();
			this.value = value;
			this.next = next;
		}
		
		Node(int value) {
			this(value, null);
		}
		
		@Override
		public String toString(){
			return String.valueOf(value);
		}
		
	}

}
