package com.max.algs.graph;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GraphUtilsTest {


    @Test
    public void connectedComponents() {

        Graph<String> graph = Graph.createGraph();

        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("A", "D");
        graph.addEdge("B", "G");
        graph.addEdge("C", "E");
        graph.addEdge("D", "F");
        graph.addEdge("G", "H");
        graph.addEdge("E", "H");
        graph.addEdge("F", "H");

        List<Set<String>> components1 = GraphUtils.connectedComponents(graph);

        assertEquals("Connected components count is incorrect", 1, components1.size());

        Set<String> firstComponentExpected = newHashSet("A", "B", "C", "D", "E", "F", "G", "H");
        assertEquals("1-st connected component is incorrect", firstComponentExpected, components1.get(0));

        graph.addEdge("K", "L");
        graph.addEdge("K", "O");
        graph.addEdge("L", "N");
        graph.addEdge("L", "M");
        graph.addEdge("N", "O");
        graph.addEdge("O", "P");
        graph.addEdge("M", "P");

        List<Set<String>> components2 = GraphUtils.connectedComponents(graph);

        assertEquals("Connected components count is incorrect", 2, components2.size());

        Set<String> secondComponentExpected = newHashSet("K", "L", "M", "N", "O", "P");

        assertEquals("1-st connected component is incorrect", firstComponentExpected, components2.get(0));
        assertEquals("2-nd connected component is incorrect", secondComponentExpected, components2.get(1));
    }

    private static Set<String> newHashSet(String... data) {
        Set<String> set = new HashSet<>();

        for (String value : data) {
            set.add(value);
        }

        return set;
    }


    @Test(expected = IllegalArgumentException.class)
    public void gamiltonianCyclesNullGraph() {

        GraphUtils.gamiltonianCycles(null);

    }

    @Test
    public void gamiltonianCycles() {

        Graph<String> graph = Graph.createGraph();

        graph.addEdge("A", "B");
        graph.addEdge("A", "D");

        graph.addEdge("B", "C");
        graph.addEdge("B", "E");

        graph.addEdge("C", "D");
        graph.addEdge("C", "E");

        graph.addEdge("D", "E");

        List<List<String>> cycles = GraphUtils.gamiltonianCycles(graph);

        assertEquals(4, cycles.size());

        assertArrayListEquals(new String[]{"A", "B", "C", "E", "D", "A"}, cycles.get(0));
        assertArrayListEquals(new String[]{"A", "B", "E", "C", "D", "A"}, cycles.get(1));
        assertArrayListEquals(new String[]{"A", "D", "C", "E", "B", "A"}, cycles.get(2));
        assertArrayListEquals(new String[]{"A", "D", "E", "C", "B", "A"}, cycles.get(3));
    }

    private void assertArrayListEquals(String[] expected, List<String> actual) {
        assertArrayEquals(expected, actual.toArray(new String[actual.size()]));
    }

    private void assertArrayEquals(String[] expected, String[] actual) {

        assertNotNull("'expected' array is NULL", expected);
        assertNotNull("'actual' array is NULL", actual);

        if (expected.length != actual.length) {
            throw new AssertionError("Arrays aren't equals, have different length: " + Arrays.toString(expected) + ", actual:" +
                                             " " +
                                             Arrays.toString(actual));
        }


        StringBuilder expBuf = new StringBuilder(expected.length);
        StringBuilder actualBuf = new StringBuilder(actual.length);

        boolean arraysEqual = true;

        for (int i = 0; i < expected.length; i++) {

            if (i > 0) {
                expBuf.append(", ");
                actualBuf.append(", ");
            }

            if (expected[i].equals(actual[i])) {
                expBuf.append(expected[i]);
                actualBuf.append(actual[i]);
            }
            else {
                arraysEqual = false;
                expBuf.append("[").append(expected[i]).append("]");
                actualBuf.append("[").append(actual[i]).append("]");
            }
        }

        if (!arraysEqual) {
            throw new AssertionError("Arrays aren't equals, expected: " + expBuf + ", actual: " + actualBuf);
        }

    }


}
