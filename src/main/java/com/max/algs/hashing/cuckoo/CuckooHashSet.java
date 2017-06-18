package com.max.algs.hashing.cuckoo;


import com.max.algs.hashing.universal.UniversalHashFunction;
import com.max.algs.util.NumberUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;


/**
 * Cuckoo hash set.
 * Not thread safe.
 * Doesn't allow NULL values.
 */
public class CuckooHashSet<E> extends AbstractSet<E>
        implements Set<E>, Cloneable, java.io.Serializable {

    private static final long serialVersionUID = 6620302301606922989L;


    private static final int INITIAL_CAPACITY = 8;
    private static final int NUM_OF_TABLES = 2;


    private transient int capacity = INITIAL_CAPACITY;
    private transient int size;

    private transient Object[][] tables;
    private transient UniversalHashFunction<E>[] hashFunctions;

    private long modCount;


    public CuckooHashSet(Collection<E> col) {
        this();
        addAll(col);
    }

    @SuppressWarnings("unchecked")
    public CuckooHashSet() {
        super();

        tables = new Object[NUM_OF_TABLES][];
        hashFunctions = new UniversalHashFunction[NUM_OF_TABLES];

        for (int i = 0; i < NUM_OF_TABLES; i++) {
            tables[i] = new Object[capacity];
            hashFunctions[i] = UniversalHashFunction.generate();
        }
    }


    @Override
    public Iterator<E> iterator() {
        return new CuckooHashSetIterator<E>();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object valueToDelete) {

        if (valueToDelete == null) {
            return false;
        }

        for (int tableIndex = 0; tableIndex < tables.length; tableIndex++) {


            int slot = calculateSlot((E) valueToDelete, tableIndex);

            if (valueToDelete.equals(tables[tableIndex][slot])) {
                tables[tableIndex][slot] = null;
                --size;
                ++modCount;
                return true;
            }
        }

        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E baseValue) {

        if (baseValue == null) {
            throw new IllegalArgumentException("Can't store NULL value");
        }

        if (contains(baseValue)) {
            return false;
        }

        E value = baseValue;

        int iteration = 0;


        final int maxLoopIterations = calculateMaxLoopIterations();

        MAIN_LOOP:
        while (iteration < maxLoopIterations) {

            for (int tableIndex = 0; tableIndex < tables.length; tableIndex++) {

                int slot = calculateSlot(value, tableIndex);

                if (tables[tableIndex][slot] == null) {
                    tables[tableIndex][slot] = value;
                    break MAIN_LOOP;
                }

                E temp = (E) tables[tableIndex][slot];
                tables[tableIndex][slot] = value;
                value = temp;
            }

            ++iteration;
        }

        if (iteration >= maxLoopIterations) {
            rehash();
            add(value); // 'put' will increment 'size'
        }
        else {
            ++modCount;
            ++size;
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object value) {

        for (int tableIndex = 0; tableIndex < tables.length; tableIndex++) {

            int slot = calculateSlot((E) value, tableIndex);

            Object[] table = tables[tableIndex];

            if (value.equals(table[slot])) {
                return true;
            }
        }

        return false;
    }

    /**
     * CEIL( 3 * log2(N) )
     */
    private int calculateMaxLoopIterations() {
        return Math.max(1, (int) Math.ceil(3 * NumberUtils.log2(size)));
    }

    private int calculateSlot(E value, int funcIndex) {

        int slot = hashFunctions[funcIndex].hash(value);

        if (slot < 0) {
            slot = -slot;
        }

        return slot % tables[funcIndex].length;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {

        Object[][] tempTables = tables;

        capacity <<= 1;
        size = 0;

        tables = new Object[tables.length][];

        for (int i = 0; i < tempTables.length; i++) {
            tables[i] = new Object[capacity];
        }

        for (Object[] tempTable : tempTables) {
            for (Object value : tempTable) {
                if (value != null) {
                    add((E) value);
                }
            }
        }

    }

    @SuppressWarnings({"unchecked"})
    @Override
    public CuckooHashSet<E> clone() {
        CuckooHashSet<E> result = null;
        try {
            result = (CuckooHashSet<E>) super.clone();
        }
        catch (CloneNotSupportedException e) {
            /** assert false; */
        }

        if (result == null) {
            throw new IllegalStateException("Clone returned NULL");
        }

        /** copy object state */

        result.modCount = 0L;
        result.hashFunctions = hashFunctions.clone();
        result.tables = tables.clone();

        for (int tableIndex = 0; tableIndex < tables.length; tableIndex++) {
            result.tables[tableIndex] = tables[tableIndex].clone();
        }

        return result;
    }


    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        out.writeInt(tables.length);
        out.writeInt(capacity);
        out.writeInt(size);

        /** write all stored objects */
        for (Object value : this) {
            out.writeObject(value);
        }
    }


    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        int tablesLength = in.readInt();
        capacity = in.readInt();
        int originalSize = in.readInt();

        tables = new Object[tablesLength][];
        hashFunctions = new UniversalHashFunction[tablesLength];

        for (int tableIndex = 0; tableIndex < tables.length; tableIndex++) {
            tables[tableIndex] = new Object[capacity];
            hashFunctions[tableIndex] = UniversalHashFunction.generate();
        }

        /** read all stored objects */
        for (int i = 0; i < originalSize; i++) {
            this.add((E) in.readObject());
        }
    }

    /**
     * Equals calculated based on 'tables' content and  'hashFunctions'
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }

        CuckooHashSet<?> other = (CuckooHashSet<?>) obj;

        if (tables.length != other.tables.length) {
            return false;
        }

        for (int tableIndex = 0; tableIndex < tables.length; tableIndex++) {
            if (!Arrays.equals(tables[tableIndex], other.tables[tableIndex])) {
                return false;
            }
        }

        return Arrays.equals(hashFunctions, other.hashFunctions);

    }

    /**
     * Hash calculated based on 'tables' content and  'hashFunctions'
     */
    @Override
    public int hashCode() {

        int result = 17;

        for (Object[] singleTable : tables) {
            result = 31 * result + (singleTable != null ? Arrays.hashCode(singleTable) : 0);
        }

        result = 31 * result + (hashFunctions != null ? Arrays.hashCode(hashFunctions) : 0);

        return result;
    }


    private final class CuckooHashSetIterator<U> implements Iterator<U> {

        private final long storedModCount;
        private final int originalSize;

        private int tableIndex = 0;
        private int curIndex = 0;

        private int lastTableIndex = -1;
        private int lastIndex = -1;

        private int traversedElemsCount = 0;

        private CuckooHashSetIterator() {
            this.storedModCount = modCount;
            this.originalSize = size;
        }

        @Override
        public boolean hasNext() {
            return traversedElemsCount < originalSize;
        }

        @SuppressWarnings("unchecked")
        @Override
        public U next() {

            if (storedModCount != modCount) {
                throw new ConcurrentModificationException("CuckooHashSet was modified during traversation");
            }

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            U retValue = null;
            Object[] table;

            MAIN_LOOP:
            while (tableIndex < tables.length) {

                table = tables[tableIndex];

                while (curIndex < table.length) {

                    if (table[curIndex] != null) {
                        ++traversedElemsCount;
                        retValue = (U) table[curIndex];
                        lastTableIndex = tableIndex;
                        lastIndex = curIndex;
                        ++curIndex;
                        break MAIN_LOOP;
                    }

                    ++curIndex;
                }

                ++tableIndex;
                curIndex = 0;
            }

            return retValue;
        }

        @Override
        public void remove() {

            if (lastTableIndex < 0 && lastIndex < 0) {
                throw new IllegalStateException("Call next() first");
            }

            tables[lastTableIndex][lastIndex] = null;

            --size;

            lastTableIndex = -1;
            lastIndex = -1;
        }
    }


}
