package com.max.algs.hashing.cuckoo;

import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.*;

public class CuckooHashtableTest {


    @Test
    public void testClone() {

        CuckooHashSet<String> table =
                new CuckooHashSet<>(Arrays.asList("max", "olesia", "zorro", "kotia", "cat in boots", "shrek"));

        CuckooHashSet<String> clonedTable = table.clone();

        assertFalse(table == clonedTable);

        assertTrue(table.equals(clonedTable));

        assertEquals(table.size(), clonedTable.size());

        clonedTable.add("some-1");
        assertFalse(table.equals(clonedTable));
        clonedTable.add("some-2");

        assertEquals(table.size() + 2, clonedTable.size());

        assertFalse(table.contains("some-1"));
        assertTrue(clonedTable.contains("some-1"));

        assertFalse(table.contains("some-2"));
        assertTrue(clonedTable.contains("some-2"));

        table.add("some-3");

        assertFalse(table.contains("some-1"));
        assertTrue(clonedTable.contains("some-1"));

        assertFalse(table.contains("some-2"));
        assertTrue(clonedTable.contains("some-2"));

        assertFalse(clonedTable.contains("some-3"));
        assertTrue(table.contains("some-3"));


    }


    @Test(expected = IllegalStateException.class)
    public void testIteratorRemoveWithoutNext() {
        CuckooHashSet<String> table =
                new CuckooHashSet<>(Arrays.asList("max", "olesia", "zorro", "kotia", "cat in boots", "shrek"));

        Iterator<String> it = table.iterator();

        it.remove();
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratorRemoveTwiceWithOneNext() {
        CuckooHashSet<String> table =
                new CuckooHashSet<>(Arrays.asList("max", "olesia", "zorro", "kotia", "cat in boots", "shrek"));

        Iterator<String> it = table.iterator();

        it.next();
        it.remove();
        it.remove();

    }


    @Test
    public void testIteratorRemove() {

        CuckooHashSet<String> table =
                new CuckooHashSet<>(Arrays.asList("max", "olesia", "zorro", "kotia", "cat in boots", "shrek"));

        Iterator<String> it = table.iterator();

        String value = it.next();
        it.remove();

        assertFalse(table.isEmpty());
        assertEquals(5, table.size());
        assertFalse(table.contains(value));

        for (int i = 0; i < 4; i++) {
            it.next();
            it.remove();
        }

        assertFalse(table.isEmpty());
        assertEquals(1, table.size());

        value = it.next();
        it.remove();

        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
        assertFalse(table.contains(value));
    }


    @Test(expected = ConcurrentModificationException.class)
    public void testIteratorModificationException() {

        CuckooHashSet<String> table =
                new CuckooHashSet<>(Arrays.asList("max", "olesia", "zorro", "kotia", "cat in boots", "shrek"));
        Iterator<String> it = table.iterator();

        it.next();
        table.add("some value");
        it.next();
    }


    @Test(expected = NoSuchElementException.class)
    public void testIteratorOutOfBounds() {

        String[] data = {"max", "olesia", "zorro", "kotia", "cat in boots", "shrek"};

        CuckooHashSet<String> table = new CuckooHashSet<>(Arrays.asList(data));
        Iterator<String> it = table.iterator();

        for (int i = 0; i < data.length + 1; i++) {
            it.next();
        }

    }

    @Test
    public void testIterator() {

        String[] data = {"max", "olesia", "zorro", "kotia", "cat in boots", "shrek"};

        Set<String> traversedValues = new HashSet<>();

        CuckooHashSet<String> table = new CuckooHashSet<>(Arrays.asList(data));

        Iterator<String> it = table.iterator();

        for (int i = 0; i < data.length; i++) {
            assertTrue(it.hasNext());

            String value = it.next();

            assertNotNull(value);
            assertFalse(traversedValues.contains(value));

            traversedValues.add(value);
        }

    }

    @Test
    public void checkSerialization() throws Exception {

        String[] data = {"max", "olesia", "zorro", "kotia", "cat in boots", "shrek"};

        CuckooHashSet<String> oldTable = new CuckooHashSet<>();

        for (String value : data) {
            oldTable.add(value);
        }

        ByteArrayOutputStream byteArrStream = new ByteArrayOutputStream();

        ObjectOutputStream out = new ObjectOutputStream(byteArrStream);
        out.writeObject(oldTable);


        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteArrStream.toByteArray()));

        @SuppressWarnings("unchecked")
        CuckooHashSet<String> newTable = (CuckooHashSet<String>) in.readObject();
        assertFalse(newTable == oldTable);

        assertEquals(newTable.size(), oldTable.size());


        for (String oldValue : oldTable) {
            assertTrue(newTable.contains(oldValue));
        }
    }


    @Test
    @Ignore
    public void putStringsFromFile() throws Exception {

        CuckooHashSet<String> hashTable = new CuckooHashSet<>();
        Set<String> allWords = new HashSet<>();

        Path filePath = Paths.get(CuckooHashtableTest.class.getResource("cuckoo_hashtable_test_in.txt").toURI());

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.toLowerCase().trim();
                allWords.add(line);
                hashTable.add(line);
            }
        }

        assertEquals("Hashtable size is incorrect", allWords.size(), hashTable.size());

        for (String singleWord : allWords) {
            assertTrue("Can't find value '" + singleWord + "' in  CuckooHashSet", hashTable.contains(singleWord));
        }

    }


    @Test
    public void putLotsValues() {

        CuckooHashSet<Integer> hashTable = new CuckooHashSet<>();

        Random rand = new Random();
        Set<Integer> values = new HashSet<>();

        for (int i = 0; i < 100_000; i++) {
            Integer value = rand.nextInt();
            values.add(value);
            hashTable.add(value);
        }

        assertEquals("Hashtable size is incorrect", values.size(), hashTable.size());

        for (Integer value : values) {
            assertTrue("Can't find value '" + value + "' in  CuckooHashSet", hashTable.contains(value));
        }

    }

    @Test
    public void putWithRehashing() {

        CuckooHashSet<Integer> hashTable = new CuckooHashSet<>();

        Integer[] data = {10, 6, 8, 4};

        for (Integer value : data) {
            hashTable.add(value);
        }

        for (Integer value : data) {
            assertTrue("Can't find value '" + value + "' in  CuckooHashSet", hashTable.contains(value));
        }

    }


    @Test
    public void put() {

        Set<String> hashTable = new CuckooHashSet<>();

        assertTrue(hashTable.isEmpty());
        assertEquals(0, hashTable.size());

        hashTable.add("max");

        assertFalse(hashTable.isEmpty());
        assertEquals(1, hashTable.size());
        assertTrue(hashTable.contains("max"));
        assertFalse(hashTable.contains("zorro"));


        hashTable.add("olesia");
        hashTable.add("zorro");

        assertFalse(hashTable.isEmpty());
        assertEquals(3, hashTable.size());
        assertTrue(hashTable.contains("max"));
        assertTrue(hashTable.contains("olesia"));
        assertTrue(hashTable.contains("zorro"));
        assertFalse(hashTable.contains("kotia"));

        hashTable.add("kotia");
        hashTable.add("cat in boots");
        hashTable.add("shrek");

        assertEquals(6, hashTable.size());
        assertTrue(hashTable.contains("max"));
        assertTrue(hashTable.contains("olesia"));
        assertTrue(hashTable.contains("zorro"));
        assertTrue(hashTable.contains("kotia"));
        assertTrue(hashTable.contains("cat in boots"));
        assertTrue(hashTable.contains("shrek"));

        assertFalse(hashTable.contains("maxi"));
        assertFalse(hashTable.contains("olesai"));

    }


    @Test
    public void remove() {

        String[] data = {"max", "olesia", "zorro", "kotia", "cat in boots", "shrek"};

        CuckooHashSet<String> hashTable = new CuckooHashSet<>();

        for (String value : data) {
            hashTable.add(value);
        }

        assertTrue(hashTable.remove("max"));

        assertEquals(5, hashTable.size());
        assertFalse(hashTable.isEmpty());
        assertFalse(hashTable.contains("max"));
        assertTrue(hashTable.contains("olesia"));
        assertTrue(hashTable.contains("zorro"));
        assertTrue(hashTable.contains("kotia"));
        assertTrue(hashTable.contains("cat in boots"));
        assertTrue(hashTable.contains("shrek"));

        assertFalse(hashTable.remove("maxi"));
        assertFalse(hashTable.remove(null));

        assertTrue(hashTable.remove("olesia"));
        assertTrue(hashTable.remove("zorro"));

        assertEquals(3, hashTable.size());
        assertFalse(hashTable.isEmpty());
        assertFalse(hashTable.contains("max"));
        assertFalse(hashTable.contains("olesia"));
        assertFalse(hashTable.contains("zorro"));
        assertTrue(hashTable.contains("kotia"));
        assertTrue(hashTable.contains("cat in boots"));
        assertTrue(hashTable.contains("shrek"));


        assertTrue(hashTable.remove("kotia"));
        assertTrue(hashTable.remove("cat in boots"));
        assertTrue(hashTable.remove("shrek"));

        assertEquals(0, hashTable.size());
        assertTrue(hashTable.isEmpty());
        assertFalse(hashTable.contains("max"));
        assertFalse(hashTable.contains("olesia"));
        assertFalse(hashTable.contains("zorro"));
        assertFalse(hashTable.contains("kotia"));
        assertFalse(hashTable.contains("cat in boots"));
        assertFalse(hashTable.contains("shrek"));

    }


}
