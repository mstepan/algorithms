package com.max.algs.lru;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public final class LruQueue<T> extends AbstractCollection<T> {


    private static final Object DUMP_OBJECT = new Object();
    private final int capacity;
    private final Map<T, Object> data = new LinkedHashMap<T, Object>() {

        private static final long serialVersionUID = 1705842783467251760L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<T, Object> eldest) {
            return super.size() > capacity;
        }
    };

    public LruQueue(int capacity) {
        super();
        if (capacity <= 0) {
            throw new IllegalArgumentException("'capacity' should be positive for LruQueue: " + capacity);
        }
        this.capacity = capacity;
    }


    //==== PRIVATE ====

    @Override
    public Iterator<T> iterator() {
        return data.keySet().iterator();
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean add(T value) {
        data.put(value, DUMP_OBJECT);
        return true;
    }


}
