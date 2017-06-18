package com.max.algs.ds.list;

import org.junit.Test;

import static org.junit.Assert.*;

public class CompactLinkedListTest {


    @Test
    public void add() {
        CompactLinkedList<Integer> list = new CompactLinkedList<>();

        assertEquals("[]", list.toString());

        list.add(1);
        assertEquals("[1]", list.toString());

        list.add(2);
        assertEquals("[2, 1]", list.toString());

        list.add(3);
        assertEquals("[3, 2, 1]", list.toString());
    }

    @Test
    public void delete() {

        CompactLinkedList<Integer> list = new CompactLinkedList<>();

        assertSame(null, list.delete());


        list.add(1);
        list.add(2);
        list.add(3);

        assertTrue(list.delete() == 3);
        assertEquals("[2, 1]", list.toString());
        assertTrue(list.delete() == 2);
        assertEquals("[1]", list.toString());
        assertTrue(list.delete() == 1);
        assertEquals("[]", list.toString());
        assertSame(null, list.delete());

    }


    @Test
    public void shuffleFromMiddleEvenSize() {

        CompactLinkedList<Integer> list = new CompactLinkedList<>();

        for (int i = 6; i > 0; i--) {
            list.add(i);
        }

        assertEquals("[1, 2, 3, 4, 5, 6]", list.toString());

        list.shuffleFromMiddle();

        assertEquals("[1, 4, 2, 5, 3, 6]", list.toString());


    }

    @Test
    public void shuffleFromMiddleOddSize() {

        CompactLinkedList<Integer> list = new CompactLinkedList<>();

        for (int i = 7; i > 0; i--) {
            list.add(i);
        }

        assertEquals("[1, 2, 3, 4, 5, 6, 7]", list.toString());

        list.shuffleFromMiddle();

        assertEquals("[1, 5, 2, 6, 3, 7, 4]", list.toString());


    }

    @Test
    public void shuffleFromMiddleEmptyList() {
        CompactLinkedList<Integer> list = new CompactLinkedList<>();

        assertEquals("[]", list.toString());

        list.shuffleFromMiddle();

        assertEquals("[]", list.toString());
    }


    @Test
    public void shuffleFromMiddleOneElementList() {
        CompactLinkedList<Integer> list = new CompactLinkedList<>();

        list.add(1);

        assertEquals("[1]", list.toString());

        list.shuffleFromMiddle();

        assertEquals("[1]", list.toString());
    }

    @Test
    public void shuffleFromMiddleTwoElementsList() {

        CompactLinkedList<Integer> list = new CompactLinkedList<>();

        list.add(2);
        list.add(1);

        assertEquals("[1, 2]", list.toString());

        list.shuffleFromMiddle();

        assertEquals("[1, 2]", list.toString());
    }


}
