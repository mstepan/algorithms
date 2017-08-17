package com.max.algs.graph;

import com.max.algs.ds.set.DisjointSet;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;


public final class GraphUtils {


    private GraphUtils() {
        throw new IllegalStateException("Can't instantiate utility class '" + GraphUtils.class.getName() + "'");
    }


    /**
     * Get connected components of undirected graph.
     */
    public static <T> List<Set<T>> connectedComponents(Graph<T> graph) {

        checkNotNull(graph);

        List<Set<T>> allComponents = new ArrayList<>();

        Set<T> visited = new HashSet<>();

        for (T vertex : graph.getVertexes()) {

            // new connected component found
            if (!visited.contains(vertex)) {

                Set<T> singleComponent = new HashSet<>();

                Deque<T> queue = new ArrayDeque<>();
                queue.add(vertex);

                while (!queue.isEmpty()) {

                    T curVer = queue.poll();

                    visited.add(curVer);
                    singleComponent.add(curVer);

                    for (T other : graph.getAdjVertexes(curVer)) {
                        if (!singleComponent.contains(other) && !visited.contains(other)) {
                            queue.add(other);
                            singleComponent.add(other);
                        }
                    }
                }

                allComponents.add(singleComponent);
            }
        }

        return allComponents;
    }

    public static <T> List<List<T>> gamiltonianCycles(Graph<T> graph) {
        return new HamiltonianCycle<T>(graph).findCycles();
    }


    /**
     * Kruskal minimum spanning tree algorithm.
     * <p>
     * time: O(E*logE + E*logV)
     */
    public static <T> List<Edge<T>> minimumSpanningTree(Graph<T> graph) {

        List<Edge<T>> edges = graph.getAllEdges();
        Collections.sort(edges, Edge.WEIGHT_ASC_CMP);

        List<Edge<T>> msTree = new ArrayList<>();

        DisjointSet<T> vertexesSet = new DisjointSet<>();

        List<T> vertexes = graph.getVertexes();

        for (T vertex : vertexes) {
            vertexesSet.add(vertex);
        }

        for (int i = 0; i < edges.size(); i++) {
            Edge<T> singleEdge = edges.get(i);

            T src = singleEdge.src;
            T dest = singleEdge.dest;

            if (!vertexesSet.find(src).equals(vertexesSet.find(dest))) {
                msTree.add(singleEdge);
                vertexesSet.union(src, dest);
            }
        }

        return msTree;
    }

    /**
     * Works for directed and undirected graphs.
     */
    public static <T> boolean hasCycle(Graph<T> graph) {

        if (graph.isEmpty()) {
            return false;
        }

        if (graph.isUndirected()) {
            return hasCycleInUndirectedGraph(graph);
        }


        return hasCycleInDirectedGraph(graph);
    }

    /**
     * Detect cycle in a directed graph using topological sorting.
     */
    private static <T> boolean hasCycleInDirectedGraph(Graph<T> graph) {

        List<T> vertexes = graph.getVertexes();
        int vertexesCount = vertexes.size();

        Map<T, Integer> inDegree = new HashMap<>();

        for (T vertex : vertexes) {
            inDegree.put(vertex, 0);
        }

        for (Edge<T> edge : graph.getAllEdges()) {
            inDegree.compute(edge.dest, (key, val) -> val + 1);
        }

        Deque<T> freeVertexes = new ArrayDeque<>();

        for (Map.Entry<T, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                freeVertexes.add(entry.getKey());
            }
        }

        while (!freeVertexes.isEmpty()) {
            T vertex = freeVertexes.poll();
            --vertexesCount;

            for (T adjVer : graph.getAdjVertexes(vertex)) {
                inDegree.compute(adjVer, (key, val) -> val - 1);

                if (inDegree.get(adjVer) == 0) {
                    freeVertexes.add(adjVer);
                }
            }

        }

        return vertexesCount != 0;
    }

    /**
     * Detect cycle in undirected graph using DFS.
     */
    private static <T> boolean hasCycleInUndirectedGraph(Graph<T> graph) {

        T firstVertex = graph.getVertexes().iterator().next();

        Map<T, T> parents = new HashMap<>();
        Set<T> visited = new HashSet<>();
        Set<T> marked = new HashSet<>();

        marked.add(firstVertex);

        Deque<T> queue = new ArrayDeque<>();
        queue.add(firstVertex);

        while (!queue.isEmpty()) {
            T vertex = queue.poll();

            Set<T> neighbours = graph.getAdjVertexes(vertex);

            for (T singleNeighbour : neighbours) {

                if (visited.contains(singleNeighbour)) {

                    T curParent = parents.get(vertex);
                    if (!curParent.equals(singleNeighbour)) {
                        return true;
                    }
                }

                else if (!marked.contains(singleNeighbour)) {
                    queue.add(singleNeighbour);
                    marked.add(singleNeighbour);
                    parents.put(singleNeighbour, vertex);
                }
            }

            marked.remove(vertex);
            visited.add(vertex);
        }

        return false;
    }


}
