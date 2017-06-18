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

    public static <T> boolean hasCycle(Graph<T> graph) {

        if (graph.isEmpty()) {
            return false;
        }


        T firstVertex = graph.getVertexes().iterator().next();

        if (!graph.isDirected()) {

            // detect cycle in undirected graph using DFS
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
                        if (curParent != singleNeighbour) {
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

        }

        return false;
    }


}
