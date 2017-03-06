package com.max.algs.it;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class NestedListsIterator<T> implements Iterator<T> {

	
	public NestedListsIterator(List<Object> list) {
		super();
		
		if( list == null ){
			throw new IllegalArgumentException("'list' parameter is NULL");
		}
		
		this.curIt = list.iterator();	
		this.element = getNextElement();
	}

	@Override
	public boolean hasNext() {
		return element != null;
	}

	@Override
	public T next() {
		
		if( ! hasNext() ){
			throw new NoSuchElementException();
		}
		
		T retValue = element;		
		element = getNextElement();
		
		return retValue;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();		
	}
	
	
	/**
	 * 
	 * @return next available element or NULL if not exists
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private T getNextElement(){
		
		Object obj = null;
		
		while( true ){	
			
			if( ! curIt.hasNext() && delayedData.isEmpty() ){
				return null;
			}
			
			if( curIt.hasNext() ){	
				
				obj = curIt.next();
				
				// list (maybe nested list)	
				if( obj instanceof List ){								
					delayedData.push( curIt );
					curIt = ((List) obj).iterator();
				}
				// single element
				else {
					return (T)obj;
				}							
			}
			else {		
				curIt = delayedData.pop();
			}
		}
		
	}
	
	
	private T element;
	private Iterator<Object> curIt;	
	private final Deque<Iterator<Object>> delayedData = new ArrayDeque<Iterator<Object>>();


}
