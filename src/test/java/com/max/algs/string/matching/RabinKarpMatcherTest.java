package com.max.algs.string.matching;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

public class RabinKarpMatcherTest {

    private final RabinKarpMatcher matcher = RabinKarpMatcher.INST;
    private final IStringMatcher naiveMatcher = NaiveStringMatcher.INST;


    @Test
    public void specialTest() {
        String str = "awplgttjviccodrbwknygkcjfjljqzgdccevkjxmabxndknxqsyfbyinuxofvnmyibkonucwndoybdytmsrpuggdrrsyaluahisg";
        String pattern = "gd";
        assertEquals(createList(30, 86), matcher.validShifts(str, pattern));

        assertEquals(createList(3, 4, 5), matcher.validShifts("dlknnnnttz", "nn"));

    }

    @Test
    public void validShiftsRandom() {

        String text1 = "dlknnnnttz";
        String pattern1 = "nn";
        assertEquals(createList(3, 4, 5), matcher.validShifts(text1, pattern1));

        Random rand = new Random();

        for (int i = 0; i < 10_000; i++) {

            String text = randomCharString(10_000);
            String pattern = randomCharString(rand.nextInt(7));

//			LOG.info( text );
//			LOG.info( pattern );
//			LOG.info( matcher.validShifts(text, pattern) );	

            assertEquals(naiveMatcher.validShifts(text, pattern), matcher.validShifts(text, pattern));
        }

        assertEquals(createList(), matcher.validShifts("ojxwvxuyuodxnedszgaxwutirnpwvsnzdcqdhuhchsjonbkodvawpftkrixuergcwitnxygjmtdwliuiftrncelsejbbmlnnsiza", "jfwad"));


    }

    private String randomCharString(int length) {
        StringBuilder buf = new StringBuilder();

        Random rand = ThreadLocalRandom.current();

        int min = 'a';
        int max = 'z';

        for (int i = 0; i < length; i++) {
            buf.append((char) (min + rand.nextInt(max - min + 1)));
        }
        return buf.toString();

    }


    @Test
    public void validShifts() {
        assertEquals(createList(0, 7), matcher.validShifts("abracadabra", "abr"));
        assertEquals(createList(2), matcher.validShifts("abcabd", "cab"));
        assertEquals(createList(), matcher.validShifts("abcabd", "cad"));
        assertEquals(createList(0, 3, 9, 12), matcher.validShifts("aabaabcaxaabaabcytyuyer", "aab"));
        assertEquals(createList(1), matcher.validShifts("ize", "ze"));
        assertEquals(createList(14), matcher.validShifts("njnyibfogqcjyize", "ze"));
    }


    @Test
    public void validShiftsMulti() {

        List<String> patterns = new ArrayList<>();

        patterns.add("ra");
        patterns.add("ca");
        patterns.add("da");

        assertEquals(createList(2, 4, 6, 9), matcher.validShiftsMulti("abracadabra", patterns));
    }

    private List<Integer> createList(int... arr) {
        List<Integer> list = new ArrayList<>();

        for (int value : arr) {
            list.add(value);
        }

        return list;
    }


}
