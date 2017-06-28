package com.max.algs.graph;

import com.google.common.collect.ImmutableSet;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Find clique of max size in undirected graph.
 */
public final class CliqueFinder {

    private static final Logger LOG = Logger.getLogger(CliqueFinder.class);

    private CliqueFinder() {
        throw new AssertionError("Can't instantiate utility class");
    }

    public static <T> Set<T> findMaxClique(Graph<T> graph) {

        ImmutableSet.Builder<T> clique = ImmutableSet.builder();

        List<VertexWithDegree<T>> vertexesWithDegree = new ArrayList<>();

        for (T vertex : graph.getVertexes()) {
            int degree = graph.getAdjVertexes(vertex).size();
            vertexesWithDegree.add(new VertexWithDegree<T>(vertex, degree));
        }

        // sort vertexes by degree in ASC order
        Collections.sort(vertexesWithDegree, VertexWithDegree.DEGREE_ASC_CMP);

        for (VertexWithDegree<T> vertexAndDegree : vertexesWithDegree) {

            T curVer = vertexAndDegree.vertex;

            Set<T> adjVertexes = graph.getAdjVertexes(curVer);
            adjVertexes.add(curVer);

            if (isAllConnected(graph, adjVertexes)) {

                clique.addAll(adjVertexes);

                return clique.build();
            }
            else {
                graph.removeVertex(curVer);
            }
        }

        return clique.build();
    }

    private static <U> boolean isAllConnected(Graph<U> graph, Set<U> vertexes) {

        for (U ver : vertexes) {

            Set<U> adjVer = graph.getAdjVertexes(ver);

            int degree = adjVer.size();

            if (degree != vertexes.size() - 1) {
                return false;
            }

            for (U other : adjVer) {
                if (!vertexes.contains(other)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        try {
            Graph<Integer> graph = Graph.createGraph();

            graph.addEdge(1, 6);
            graph.addEdge(1, 5);
            graph.addEdge(6, 5);

            graph.addEdge(1, 2);
            graph.addEdge(1, 4);

            graph.addEdge(2, 4);
            graph.addEdge(2, 3);
            graph.addEdge(3, 4);

            graph.addEdge(3, 7);
            graph.addEdge(2, 7);
            graph.addEdge(4, 7);

            Set<Integer> clique = CliqueFinder.findMaxClique(graph);

            System.out.println("Max clique: " + clique);
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private static final class VertexWithDegree<U> {

        private static final Comparator<VertexWithDegree<?>> DEGREE_ASC_CMP = new Comparator<VertexWithDegree<?>>() {
            @Override
            public int compare(VertexWithDegree<?> first, VertexWithDegree<?> second) {
                return -Integer.compare(first.degree, second.degree);
            }
        };

        final U vertex;
        final int degree;

        public VertexWithDegree(U vertex, int degree) {
            this.vertex = vertex;
            this.degree = degree;
        }

        @Override
        public String toString() {
            return String.valueOf(vertex) + ": " + degree;
        }
    }
}
