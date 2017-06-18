package com.max.algs.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SetUtilsTest {

    @Test
    public void union() {
        List<Integer> set1 = createList(1, 2, 3);
        List<Integer> set2 = createList(2, 3);
        assertEquals(createList(1, 2, 3), SetUtils.union(set1, set2));

        set1 = createList(1, 2, 3);
        set2 = createList(4, 5);
        assertEquals(createList(1, 2, 3, 4, 5), SetUtils.union(set1, set2));
    }

    @Test
    public void difference() {
        List<Integer> set1 = createList(1, 2, 3);
        List<Integer> set2 = createList(2, 3);
        assertEquals(createList(1), SetUtils.difference(set1, set2));

        set1 = createList(2, 3);
        set2 = createList(2, 3);
        assertEquals(createList(), SetUtils.difference(set1, set2));

        set1 = createList();
        set2 = createList();
        assertEquals(createList(), SetUtils.difference(set1, set2));

        set1 = createList(1, 2, 3);
        set2 = createList(4, 5);
        assertEquals(createList(1, 2, 3), SetUtils.difference(set1, set2));

    }

    private List<Integer> createList(int... values) {
        List<Integer> data = new ArrayList<>();
        for (int val : values) {
            data.add(val);
        }

        return data;
    }


}
