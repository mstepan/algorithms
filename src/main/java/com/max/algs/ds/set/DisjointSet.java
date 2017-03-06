package com.max.algs.ds.set;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * Disjoint ds data structure with pass compression during 'find' call.
 *
 * DS suitable only for ds find and union.
 * Can't remove or un join sets.
 *
 * Not thread safe.
 *
 */
public class DisjointSet<T> implements Serializable, Iterable<T> {

	private static final long serialVersionUID = -5820359197462104080L;
	
	
	private static final int DEFAULT_INITIAL_CAPACITY = 8;
    private final Map<T, Entry<T>> entries;

    // disjoint ds count
    private int size;


    public DisjointSet(int initialCapacity){
        entries = new HashMap<>(initialCapacity);
    }

    public DisjointSet(){
        this(DEFAULT_INITIAL_CAPACITY);
    }


    @Override
    public Iterator<T> iterator() {
        return entries.keySet().iterator();
    }

    public int size(){
        return size;
    }



    public T find(T value){

        Entry<T> set = findCompressingPath(value);

        if( set != null ){
            return set.value;
        }

        return null;
    }


    public boolean union(T value1, T value2){

        Entry<T> set1 = findCompressingPath(value1);
        Entry<T> set2 = findCompressingPath(value2);

        if( set1 == null || set2 == null ){
            throw new IllegalArgumentException("Can't union sets, one ds NULL ( or both sets are NULL )");
        }

        // sets already joined, skip
        if( set1 == set2 ){
            return false;
        }

        // check if set1 > set2, if not, change them
        if( set2.size > set1.size ){
            Entry<T> temp = set1;
            set1 = set2;
            set2 = temp;
        }

        // join two sets, smaller ds join bigger one
        set1.size += set2.size;
        set2.next = set1;
        --size;
        return true;
    }


    public boolean add(T value){

        if( value == null ){
            throw new IllegalArgumentException("Can't add NULL value");
        }

        // skip if duplicate found
        if( entries.containsKey(value) ){
            return false;
        }

        entries.put(value, new Entry<>(value) );
        ++size;
        return true;
    }


    @Override
    public String toString(){
        StringBuilder buf = new StringBuilder( entries.size() << 1 );

        buf.append("[");

        int i = 0;
        for( T value : this ){
            if( i != 0 ){
                buf.append(", ") ;
            }
            buf.append(value);
            ++i;
        }

        buf.append("]");

        return buf.toString();
    }

    private Entry<T> findCompressingPath(T value){

        final Entry<T> baseEntry =  entries.get(value);

        if( baseEntry == null ){
            return null;
        }

        // find root entry
        Entry<T> rootEntry = traverseTillRoot(baseEntry);

        // do path compression
        Entry<T> entry = baseEntry;
        Entry<T> temp;

        while( entry.next != rootEntry && entry.next != null ){
            temp = entry.next;
            entry.next = rootEntry;
            entry = temp;
        }

        return rootEntry;
    }

    private Entry<T> traverseTillRoot(Entry<T> entry){
        assert entry != null : "NULL entry passed for traverseTillRoot";
        while( entry.next != null ){
            entry = entry.next;
        }
        return entry;
    }


    //==== NESTED ====

    private static class Entry<U> implements Serializable {


		private static final long serialVersionUID = 2618504167761914206L;
		
		U value;
        int size;
        Entry<U> next;

        Entry(U value) {
            this.value = value;
            this.size = 1;
        }

        @Override
        public String toString(){
            return value + ", size: " + size;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
            	return true;
            }
            if (o == null || getClass() != o.getClass()) {
            	return false;
            }

            Entry<?> entry = (Entry<?>) o;

            return !(value != null ? !value.equals(entry.value) : entry.value != null);

        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }


}
