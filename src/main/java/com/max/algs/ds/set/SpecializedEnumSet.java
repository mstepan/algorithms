package com.max.algs.ds.set;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import sun.misc.SharedSecrets;


/**
 * 
 * Not thread safe.
 * 
 * 
 */
@SuppressWarnings("restriction")
public class SpecializedEnumSet<K extends Enum<K>> extends AbstractSet<K> implements Serializable, Set<K> {





	private static final long serialVersionUID = -700204651453745040L;

	private final transient K[] universe;
	
	/**
	 * @serial 
	 */
	private long data;
	private int size;
	
	private transient long modCount;
	
	
	public SpecializedEnumSet(SpecializedEnumSet<K> another){
		universe = another.universe;
		data = another.data;
		modCount = another.modCount;
	}
	
	public SpecializedEnumSet(Class<K> enumClazz){
		this.universe = getKeyUniverse( enumClazz );		
		if( universe.length > Long.SIZE ){
			throw new IllegalArgumentException("Very huge enum passed");
		}
	}
	
	
	@Override
	public boolean add(K value){
		
		boolean ret = contains(value);
		
		data |= mask(value);
		
		if( !ret ){
			++size;
			++modCount;
		}
		
		return ret;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object value){
		return (data & mask((K)value) ) > 0;		
	}
	
	
	@Override
	public int size() {
		return size;
	}
		
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object value) {
		
		boolean ret = contains(value); 
		
		data &= ~mask((K)value);
		
		if( ret ){
			--size;
			++modCount;
		}

		return ret;
	}

	@Override
	public Iterator<K> iterator() {
		return new SpecializedIterator( modCount );
	}
	
	
	private int mask( K value ){
		return 1 << value.ordinal();
	}

	
	private static <K extends Enum<K>> K[] getKeyUniverse(Class<K> keyType) {
		return SharedSecrets.getJavaLangAccess().getEnumConstantsShared(keyType);
	}

	@Override
	public String toString(){
		
		StringBuilder buf = new StringBuilder();
		buf.append("{");
		
		for( K enumValue : universe ){
			if( contains(enumValue) ){
				if( buf.length() > 1 ){
					buf.append(",");
				}
				buf.append( enumValue.name() );
			}			
		}
		
		buf.append("}");
		
		return buf.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + (int) (data ^ (data >>> 32));
		return result;
	}


	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null || getClass() != obj.getClass() ){
			return false;
		}
		SpecializedEnumSet other = (SpecializedEnumSet) obj;
		if (data != other.data)
			return false;
		return true;
	}	
	
	
	
	/**
	 * 
	 * Fail-fast enum set iterator.
	 * 
	 */
	private final class SpecializedIterator implements Iterator<K> {
		
		private long modCountCopy;
		private int cur;
		private K lastValue;
				

		public SpecializedIterator(long modCount) {
			super();
			this.modCountCopy = modCount;
		}
		

		@Override
		public boolean hasNext() {
			return cur < size;
		}

		@Override
		public K next() {
			
			if( !hasNext() ){
				throw new NoSuchElementException();
			}
			
			checkModCount();
			
			int index = cur;			
			while( true ){
				
				int mask = 1 << index;
				
				if( (SpecializedEnumSet.this.data & mask) > 0 ){
					lastValue = SpecializedEnumSet.this.universe[index];
					break;
				}
				
				++index;
			}
			
			return lastValue;
		}

		@Override
		public void remove() {
			
			if( lastValue == null ){
				throw new IllegalStateException("Call 'next' first");
			}
			
			checkModCount();
			
			SpecializedEnumSet.this.remove( lastValue );	
			++modCountCopy;
			
			lastValue = null;
		}
		
		
		private void checkModCount(){
			if( modCountCopy != modCount ){
				throw new ConcurrentModificationException();
			}
		}
		
	}
	

}
