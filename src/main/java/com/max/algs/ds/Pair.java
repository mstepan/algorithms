package com.max.algs.ds;

import java.io.Serializable;
import java.util.Comparator;
import java.util.concurrent.atomic.LongAdder;

/**
 * Immutable pair class.
 *
 * @param <T>
 * @author Maksym Stepanenko.
 */
public class Pair<K, V> implements Serializable {

    public static final Comparator<Pair<Integer, LongAdder>> KEY_COMPARATOR = new Comparator<Pair<Integer, LongAdder>>() {

        @Override
        public int compare(Pair<Integer, LongAdder> o1, Pair<Integer, LongAdder> o2) {
            return Integer.compare(o1.first, o2.first);
        }

    };

    private static final long serialVersionUID = -6384976076282848373L;

    protected final K first;
    protected final V second;

    private int hash;

    public Pair(K first, V second) {
        super();
        this.first = first;
        this.second = second;
    }

    public static <T> Pair<T, T> of(T val1, T val2) {
        return new Pair<>(val1, val2);
    }

    public static <T> Pair<T, T> empty() {
        return new Pair<>(null, null);
    }

    public K getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    @Override
    public int hashCode() {

        if (hash == 0) {
            int res = 1;
            res = 31 * res + ((first == null) ? 0 : first.hashCode());
            res = 31 * res + ((second == null) ? 0 : second.hashCode());
            hash = res;
        }

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pair<?, ?> other = (Pair<?, ?>) obj;
        if (first == null) {
            if (other.first != null)
                return false;
        }
        else if (!first.equals(other.first))
            return false;
        if (second == null) {
            if (other.second != null)
                return false;
        }
        else if (!second.equals(other.second))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

}
