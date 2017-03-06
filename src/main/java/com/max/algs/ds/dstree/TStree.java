package com.max.algs.ds.dstree;


/**
 * Ternary search tree.
 *
 * @param <V>
 */
public class TStree<V> {

	// logical number of inserted key-value pairs
	private int size;	
	
	private Entry<V> root;
	
	public V put(String key, V value){
		
		if( value == null ){
			throw new IllegalArgumentException("Can't store NULL value"); 
		}
		
		char[] keyChars = key.toCharArray();
		int index = 0;
				
		if( root == null ){
			root = new Entry<V>();
			root.ch = keyChars[index];			
			insert( root, keyChars, index+1, value );
			++size;	
			return null;
		}

		
		V retValue = findReplace(root, keyChars, index, value);
		
		if( retValue == null ){
			++size;	
		}
		
		return retValue;			
	}
	

	
	
	public V getValue(String key){
		
		char[] keyChars = key.toCharArray();
		int index = 0;
		
		Entry<V> entry = root;
		
		while( entry != null && index < keyChars.length ) {
			
			char searchCh = keyChars[index];
			
			// character equals, go middle link
			if( searchCh == entry.ch ){				
				if( index == keyChars.length - 1 ){
					return entry.value;
				}				
				entry = entry.middle;
				++index;
			}
			// character less, go left link
			else if( searchCh < entry.ch ){
				entry = entry.left;
			}
			// character greater, go right link
			else {
				entry = entry.right;
			}	
			
			
		}		
		
		return null;
	}
	
	
	public V delete(String key){
		return null;
	}

	public int size() {
		return size;
	}
	
	public boolean isEmpty(){
		return size == 0;
	}
	
	//==== PRIVATE ====
	
	private V findReplace(Entry<V> startNode, char[] keyChars, int index, V value) {		
		
		Entry<V> prev = null;
		Entry<V> cur = startNode;
		char searchCh;
		
		while( index < keyChars.length  && cur != null ) {
			
			searchCh = keyChars[index];			
			prev = cur;
			
			// equals
			if( searchCh == cur.ch ){	
				cur = cur.middle;
				++index;
			}
			// less
			else if(searchCh < cur.ch){
				cur = cur.left;
			}
			// greater
			else {
				cur = cur.right;
			}						
		}
		
		
		if( index >= keyChars.length ){
			V retValue = prev.value;
			prev.value = value;		
			return retValue;
		}
		
			
		Entry<V> newEntry = new Entry<V>();
		newEntry.ch = keyChars[index];
			
		// leaf node, this key is a PREFIX for another key 
		if( prev.value != null ){
			prev.middle = newEntry;
		}
		// not leaf node
		else {			
			if( newEntry.ch == prev.ch ){	
				prev.middle = newEntry;
			}
			// less
			else if(newEntry.ch < prev.ch){
				prev.left = newEntry;
			}
			// greater
			else {
				prev.right = newEntry;
			}
		}
			
		return insert( newEntry, keyChars, index+1, value );	
	}


	private V insert( Entry<V> startNode, char[] keyChars, int index, V value ){
		
		Entry<V> prev = startNode;
		
		while( index < keyChars.length ){
			
			Entry<V> newEntry = new Entry<V>();
			newEntry.ch  = keyChars[index];
			
			prev.middle = newEntry;	
			prev = prev.middle;
			
			++index;
		}
		
		V retValue = prev.value;
		prev.value = value;		
		return retValue;
	}
	
	//==== NESTED classes === 
	
	private static final class Entry<V> { 
		
		char ch;
		V value;
		
		Entry<V> left;
		Entry<V> middle;
		Entry<V> right;
		
		
		@Override
		public String toString(){
			return String.valueOf( ch ) + ", value = " + value;
		}
		
	}
	
}
