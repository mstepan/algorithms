package com.max.algs.graph;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class GraphTest {

    private static final Logger LOG = Logger.getLogger(GraphTest.class);

    @Ignore
    @Test
    public void vertexCoverage() {

        Graph<Character> graph = Graph.createGraph();

        graph.addEdge('a', 'b');
        graph.addEdge('b', 'c');
        graph.addEdge('c', 'd');
        graph.addEdge('c', 'e');
        graph.addEdge('e', 'd');
        graph.addEdge('e', 'f');
        graph.addEdge('f', 'd');
        graph.addEdge('d', 'g');

        assertEquals(Arrays.asList('b', 'd', 'e'), graph.vertexCoverageBruteforce());
        assertEquals(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f'), graph.vertexCoverageApproximation());
    }

    @Ignore
    @Test
    public void vertexCoverageRandomGraphs() {
        for (int i = 0; i < 100; i++) {
            Graph<Integer> graph = GraphFactory.createRandomGraph(10, 50);

            List<Integer> coverageBruteforce = graph.vertexCoverageBruteforce();
            List<Integer> coverageApproximation = graph.vertexCoverageApproximation();

            assertTrue(coverageApproximation.size() <= 2 * coverageBruteforce.size());
        }
    }


    @Test
    public void createRandomGraph() {
        Graph<Integer> graph = GraphFactory.createRandomGraph(10, 10);
        LOG.info(graph);
    }


    @Test
    public void addEdge() {

        Graph<Integer> graph = Graph.createGraph();

        assertFalse(graph.hasEdge(1, 2));
        assertFalse(graph.hasEdge(2, 1));

        assertFalse(graph.hasEdge(1, 3));
        assertFalse(graph.hasEdge(3, 1));

        graph.addEdge(1, 2);

        assertTrue(graph.hasEdge(1, 2));
        assertTrue(graph.hasEdge(2, 1));
        assertFalse(graph.hasEdge(1, 3));
        assertFalse(graph.hasEdge(3, 1));

        graph.addEdge(3, 1);

        assertTrue(graph.hasEdge(1, 2));
        assertTrue(graph.hasEdge(2, 1));
        assertTrue(graph.hasEdge(1, 3));
        assertTrue(graph.hasEdge(3, 1));
    }

    @Test
    public void getAdjVertexes() {

        Graph<Integer> graph = Graph.createGraph();
        graph.addEdge(1, 2);
        graph.addEdge(3, 1);

        Set<Integer> adjVertexes = graph.getAdjVertexes(1);

        assertEquals(2, adjVertexes.size());
        assertTrue(adjVertexes.contains(2));
        assertTrue(adjVertexes.contains(3));
    }


}
