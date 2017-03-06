package com.max.algs.graph;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EulerTourTest {

    @Test
    public void buildTourSingleConnectedComponent() {

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

        List<String> tour = EulerTour.buildTour(graph);

        assertNotNull(tour);

        List<String> expectedTour = Arrays.asList("A", "D", "F", "H", "G", "B", "A", "C", "E", "H");
        assertEquals(expectedTour, tour);

    }

    @Test
    public void buildTourTwoConnectedComponents() {

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

        graph.addEdge("K", "L");
        graph.addEdge("K", "O");
        graph.addEdge("L", "N");
        graph.addEdge("L", "M");
        graph.addEdge("N", "O");
        graph.addEdge("O", "P");
        graph.addEdge("M", "P");

        List<String> tour = EulerTour.buildTour(graph);

        List<String> emptyEulerTour = new ArrayList<String>();
        assertNotNull(tour);
        assertEquals(emptyEulerTour, tour);
    }

}
