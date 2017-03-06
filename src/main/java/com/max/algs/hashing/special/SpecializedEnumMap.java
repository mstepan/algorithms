package com.max.algs.hashing.special;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

//import sun.misc.*; // prior to Java 9, use this
//import jdk.internal.misc.*; // since Java 9, use this instead
import sun.misc.SharedSecrets;


@SuppressWarnings("restriction")
public class SpecializedEnumMap<K extends Enum<K>, V> implements Serializable {
	


	private static final long serialVersionUID = -1397198286222318155L;
	
	private int size;
	private final Class<K> clazz;
	
	private transient K[] keys;		
	private transient V[] values;	

	
	@SuppressWarnings("unchecked")
	public SpecializedEnumMap( Class<K> enumClazz ) {			
		this.clazz = enumClazz;
		keys = getKeyUniverse(enumClazz);	
		values = (V[])new Object[ keys.length ];		
	}
	
	
	
	public V put(K key, V value ){
		
		checkKeyForNull( key );
		
		if( value == null ){
			throw new IllegalArgumentException("NULL ke yor value detected");
		}
		
		int index = key.ordinal();
		
		V prevValue = values[ index ];
		
		values[ index ] = value;
		
		
		if( prevValue == null ){
			++size;
		}
		
		return prevValue;		
	}
	
	
	public V get(K key){
		checkKeyForNull(key);
		return values[ key.ordinal() ];
	}
	
	
	public boolean contains( K key ){
		checkKeyForNull(key);
		return values[ key.ordinal() ] != null;
	}
	
	public boolean remove(K key){
		checkKeyForNull(key);
		
		int index = key.ordinal();
		
		V val = values[index];
		values[index] = null;
		
		if( val != null ){
			--size;
		}
		
		return val != null;
	}
	
	public int size(){
		return size;
	}
	
	public boolean isEmpty(){
		return size == 0;
	}
	
	@Override
	public String toString(){
		
		StringBuilder buf = new StringBuilder( 32 * size + 64 );
		
		buf.append("{");
		
		
		
		for( int i = 0; i < values.length; i++ ){
			
			if( values[i] != null ){
				
				if( buf.length() > 1 ){
					buf.append(",");
				}
				
				buf.append( keys[i] + " = " + values[i] );
				
				
			}
			
			
		}
		
		buf.append("}, size: ").append(size);
		buf.append(", capacity: ").append( values.length );
		
		return buf.toString();
	}

	private void checkKeyForNull(K key){
		if( key == null ){
			throw new IllegalArgumentException("NULL key passed");
		}
	}
	
	
	private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();        
        
        for( int i = 0; i < values.length && size > 0; i++ ){
        	if( values[i] != null ){
        		oos.writeInt( i );
        		oos.writeObject( values[i] );
        		--size;
        	}
        }
    }
	
	/**
	 * This code is SUN/Oracle jvm specific
	 * 
	 * @param keyType
	 * @return
	 */
	private static <K extends Enum<K>> K[] getKeyUniverse(Class<K> keyType) {
        return SharedSecrets.getJavaLangAccess()
                                        .getEnumConstantsShared(keyType);
    }

	@SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();  
        
        keys = getKeyUniverse(clazz);        
      
        values = (V[])new Object[keys.length];
        
        for( int i = 0; i < size; i++ ){
        	int index = ois.readInt();
        	V value = (V)ois.readObject();
        	values[ index ] = value;
        }

    }

}
