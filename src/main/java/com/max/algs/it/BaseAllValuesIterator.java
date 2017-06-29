package com.max.algs.it;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.checkArgument;


public final class BaseAllValuesIterator implements Iterator<String> {

    private static final char ZERO_CH = '0';

    private final int base;
    private final char[] number;
    private boolean hasMoreValues = true;

    public BaseAllValuesIterator(int base, int length) {
        checkArgument(base > 1 && base < 11, "Incorrect base value");
        this.base = base;
        this.number = new char[length];
        Arrays.fill(number, ZERO_CH);
    }

    @Override
    public boolean hasNext() {
        return hasMoreValues;
    }

    @Override
    public String next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        String res = numberInReverseOrder();
        hasMoreValues = incNumber();

        return res;
    }

    private String numberInReverseOrder() {
        StringBuilder buf = new StringBuilder(number.length);

        for (int i = number.length - 1; i >= 0; --i) {
            buf.append(number[i]);
        }

        return buf.toString();
    }

    private boolean incNumber() {

        int carry = 1;

        for (int i = 0; i < number.length && carry != 0; ++i) {

            int curRes = (number[i] - ZERO_CH) + carry;

            number[i] = (char) (ZERO_CH + (curRes % base));
            carry = curRes / base;
        }

        // if carry == 1, overflow in last position occurred -> this means we have generated all possible values
        return carry == 0;
    }
}