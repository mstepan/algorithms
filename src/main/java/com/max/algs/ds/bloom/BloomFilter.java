package com.max.algs.ds.bloom;

import com.max.algs.hashing.HashUtils;
import com.max.algs.util.BitUtils;
import com.max.algs.util.MathUtils;

import java.util.AbstractSet;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Bloom filter.
 * Not thread safe.
 * <p>
 * For general idea see: https://en.wikipedia.org/wiki/Bloom_filter
 * For hashing strategy see: https://www.eecs.harvard.edu/~michaelm/postscripts/tr-02-05.pdf
 */
public class BloomFilter<E> extends AbstractSet<E> implements Set<E>, Cloneable, java.io.Serializable {

    private static final long serialVersionUID = 2187869305918333147L;

    private static final int POSITIVE_MASK = 0x7F_FF_FF_FF;

    private static final double MIN_FALSE_POSITIVE_PROBABILITY = 0.0001; // 0.01%
    private static final double MAX_FALSE_POSITIVE_PROBABILITY = 1.0; // 100%

    private static final double DEFAULT_FALSE_POSITIVE_PROBABILITY = 0.001; // 0.1%

    private final int dataCapacityMask;// always power of '2' - 1
    private final BitSet data;

    private final int hashFunctionsCount;
    private final int[] indexes;

    private final double falsePositiveProbability;

    private int size;

    public BloomFilter(int elementsCount) {
        this(elementsCount, DEFAULT_FALSE_POSITIVE_PROBABILITY);
    }

    public BloomFilter(int elementsCount, double expectedFalsePositiveProbability) {
        checkArgument(elementsCount > 0, "'elementsCount' can't be negative");

        checkArgument(Double.compare(expectedFalsePositiveProbability, MIN_FALSE_POSITIVE_PROBABILITY) >= 0 &&
                        Double.compare(expectedFalsePositiveProbability, MAX_FALSE_POSITIVE_PROBABILITY) <= 0,
                "'expectedFalsePositiveProbability' should be in range [%s; %s], actual = %s",
                MIN_FALSE_POSITIVE_PROBABILITY, MAX_FALSE_POSITIVE_PROBABILITY, expectedFalsePositiveProbability);

        int bitsCount = BitUtils.ceil2(elementsCount);

        while (true) {

            bitsCount <<= 1;

            int curHashFunctionsCount = (int) Math.ceil((bitsCount / elementsCount) * MathUtils.ln(2.0));

            double curFalsePositiveProbability =
                    Math.pow(1.0 - Math.pow(Math.E, -curHashFunctionsCount * elementsCount / bitsCount), curHashFunctionsCount);

            if (Double.compare(curFalsePositiveProbability, expectedFalsePositiveProbability) <= 0) {

                falsePositiveProbability = curFalsePositiveProbability;

                dataCapacityMask = bitsCount - 1;
                data = new BitSet(bitsCount);

                hashFunctionsCount = curHashFunctionsCount;
                indexes = new int[hashFunctionsCount];

                break;
            }
        }
    }

    @Override
    public boolean add(E value) {
        for (int index : getIndexes(value)) {
            data.set(index);
        }

        ++size;
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object value) {
        for (int index : getIndexes((E) value)) {
            if (!data.get(index)) {
                return false;
            }
        }

        return true;
    }

    private int[] getIndexes(E value) {

        int h1 = HashUtils.murmur3Hash(String.valueOf(value));
        int h2 = HashUtils.murmur3Hash(String.valueOf(h1));

        for (int i = 0; i < hashFunctionsCount; i++) {
            indexes[i] = ((h1 + i * h2) & POSITIVE_MASK) & dataCapacityMask;
        }

        return indexes;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException(BloomFilter.class.getCanonicalName() +
                " doesn't store elements, so iterator is not supported");
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        return "bitsCount = " + (dataCapacityMask + 1) +
                ", hashFunctionsCount = " + hashFunctionsCount +
                ", falsePositiveProbability = " + falsePositiveProbability;
    }
}
