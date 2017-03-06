package com.max.algs.cmp;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Order characters according to predefined order specified by string.
 * If character not specified in mapping, use lexicographical order.
 */
public final class StringOrderComparator implements Comparator<Character>, Serializable {

    private static final long serialVersionUID = -9065348383945574875L;

    private final Map<Character, Integer> chOrder = new HashMap<>();

    public StringOrderComparator(String order) {
        checkArgument(order != null, "null 'order' string passed");
        for (int i = 0, orderLength = order.length(); i < orderLength; i++) {
            chOrder.put(order.charAt(i), i);
        }
    }

    @Override
    public int compare(Character ch1, Character ch2) {
        return Integer.compare(getOrderForChar(ch1), getOrderForChar(ch2));
    }

    private int getOrderForChar(Character ch) {
        Integer order = chOrder.get(ch);

        if (order != null) {
            return order;
        }

        return (int) ch;
    }
}
