package com.max.algs.it;

import com.max.algs.util.ArrayUtils;
import com.max.algs.util.BitUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Generate all subset of a set (2^n) with min change property.
 */
public class SubsetIterator<T> implements Iterator<T[]> {


    private final T[] originalData;
    private final int originalSetSize;

    private final Iterator<Integer> grayCodesIt;

    public SubsetIterator(T[] originalSet) {

        if (originalSet == null) {
            throw new IllegalArgumentException("'NULL' originalSet passed");
        }

        this.originalData = Arrays.copyOf(originalSet, originalSet.length);
        this.originalSetSize = originalSet.length;
        grayCodesIt = new BinaryGrayCodesIterator(originalData.length);
    }


    public SubsetIterator(Collection<T> originalSet) {

        if (originalSet == null) {
            throw new IllegalArgumentException("'NULL' originalSet passed");
        }

        this.originalData = ArrayUtils.create(originalSet);
        this.originalSetSize = originalSet.size();
        grayCodesIt = new BinaryGrayCodesIterator(originalData.length);
    }

    @Override
    public boolean hasNext() {
        return grayCodesIt.hasNext();
    }

    @Override
    public T[] next() {

        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        T[] subset = generateSubset(grayCodesIt.next());
        return subset;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();

    }


    private T[] generateSubset(int mask) {

        T[] subset = ArrayUtils.create(BitUtils.computeParity(mask));

        int index = 0;

        for (int i = 0; i < originalSetSize && mask != 0; i++) {

            if ((mask & 1) != 0) {
                subset[index] = originalData[i];
                ++index;
            }

            mask >>>= 1;
        }


        return subset;
    }


}
