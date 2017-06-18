package com.max.algs.string.matching;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShiftAndMatcherTest {


    private static final IStringMatcher matcher = ShiftAndMatcher.INST;

    @Test
    public void validShifts() {
        assertEquals(createList(0, 7), matcher.validShifts("abracadabra", "abra"));
        assertEquals(createList(), matcher.validShifts("abcabd", "cad"));
    }


    private List<Integer> createList(int... arr) {
        List<Integer> list = new ArrayList<>();

        for (int value : arr) {
            list.add(value);
        }

        return list;
    }


}
