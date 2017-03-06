package com.max.functional;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Immutable stack implementation with constant time for all operations.
 *
 * @param <T>
 */
public class ImmutableStack<T> extends AbstractCollection<T> implements
		Cloneable, java.io.Serializable {

	private static final long serialVersionUID = -5660227784875705882L;

	private final T element;
	private final ImmutableStack<T> next;
	private final int size;

	public ImmutableStack() {
		element = null;
		next = null;
		size = 0;
	}

	private ImmutableStack(T element, ImmutableStack<T> next, int newSize) {
		super();
		this.element = element;
		this.next = next;
		this.size = newSize;
	}

	/**
	 * Time: O(1)
	 * 
	 * @param newValue
	 * @return
	 */
	public ImmutableStack<T> push(T newValue) {
		return new ImmutableStack<>(newValue, this, size + 1);
	}

	/**
	 * Time: O(1)
	 * 
	 * @return
	 */
	public ImmutableStack<T> pop() {
		checkNotEmpty();
		return next;
	}

	/**
	 * Time: O(1)
	 * 
	 * @return top element of a stack.
	 */
	public T top() {
		checkNotEmpty();
		return element;
	}

	/**
	 * Time: O(1)
	 * 
	 * @return stack size.
	 */
	@Override
	public int size() {
		return size;
	}

	private void checkNotEmpty() {
		if (isEmpty()) {
			throw new IllegalStateException("Stack is empty");
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new ImmutableStackIterator();
	}
	
	private final class ImmutableStackIterator implements Iterator<T> {

		ImmutableStack<T> cur = ImmutableStack.this;		
		
		@Override
		public boolean hasNext() {
			return cur.element != null;
		}

		@Override
		public T next() {	
			
			if( ! hasNext() ){
				throw new NoSuchElementException();
			}
			
			T retValue = cur.element;			
			cur = cur.next;
			return retValue;
		}		
		
	}

	private void readObject(ObjectInputStream stream)
			throws InvalidObjectException {
		throw new InvalidObjectException(
				"'ImmutableStateSerializationProxy' required");
	}

	/**
	 * Save the state of the <tt>ImmutableStack</tt> instance to a stream (that
	 * is, serialize it).
	 */
	private Object writeReplace() {
		return new ImmutableStateSerializationProxy<>(this);
	}

	/**
	 * Serialisation proxy for ImmutableStack.
	 *
	 * @param <U>
	 */
	private static final class ImmutableStateSerializationProxy<U> implements
			java.io.Serializable {

		private static final long serialVersionUID = -4350664470871340449L;

		private final List<U> rawData = new ArrayList<>();

		private ImmutableStateSerializationProxy(ImmutableStack<U> stack) {

			ImmutableStack<U> cur = stack;

			while (cur.element != null) {
				rawData.add(cur.element);
				cur = cur.next;
			}
			/** Reverse the content so that we can deserialize easily */
			Collections.reverse(rawData);
		}

		/**
		 * Reconstitute the <tt>ImmutableStack</tt> instance from a stream (that
		 * is, deserialize it).
		 */
		private Object readResolve() {
			ImmutableStack<U> stack = new ImmutableStack<>();

			for (U singleEntity : rawData) {
				stack = stack.push(singleEntity);
			}
			return stack;
		}

	}

}
