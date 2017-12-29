package com.max.algs;


import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


public final class AlgorithmsMain {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());


    private static final class DirectAcyclicGraph {

        private static final Random RAND = ThreadLocalRandom.current();

        private final Map<String, List<EdgeWithWeight>> adjList = new HashMap<>();

        public static DirectAcyclicGraph generate(String[] vertexes) {
            checkNotNull(vertexes, "null 'vertexes' passed");

            DirectAcyclicGraph graph = new DirectAcyclicGraph();

            for (String singleVertex : vertexes) {
                graph.addVertex(singleVertex);
            }

            // add each edge with probability 5%
            for (int i = 0; i < vertexes.length - 1; ++i) {
                for (int j = i + 1; j < vertexes.length; ++j) {
                    if (RAND.nextInt(100) < 5) {
                        graph.addEdge(vertexes[i], vertexes[j], RAND.nextInt(100) + RAND.nextInt(100));
                    }
                }
            }

            Set<String> baseVertexes = graph.getSourceVertexes();

            String prev = vertexes[0];

            for (int i = 1; i < vertexes.length; ++i) {

                String cur = vertexes[i];

                if (baseVertexes.contains(cur)) {
                    graph.addEdge(prev, cur, 1 + RAND.nextInt(15));
                    prev = cur;
                }
            }
            return graph;
        }

        private boolean isConnected() {

            Set<String> marked = new HashSet<>();

            Deque<String> stack = new ArrayDeque<>();

            String firstVertex = getSourceVertexes().iterator().next();
            marked.add(firstVertex);

            stack.push(firstVertex);

            while (!stack.isEmpty()) {
                String vertex = stack.pop();

                for (EdgeWithWeight edge : getEdges(vertex)) {
                    if (!marked.contains(edge.dest)) {
                        stack.push(edge.dest);
                        marked.add(edge.dest);
                    }
                }
            }

            return marked.size() == adjList.size();
        }

        /**
         * Shortest path in DAG using topological sorting and left-to-right order of calculation.
         * <p>
         * time: O(V + E)
         * space: O(V + E)
         */
        private int shortestPath(String src, String dest) {

            assert src != null && dest != null && !src.equals(dest);

            // check both vertexes present
            if (!(adjList.containsKey(src) && adjList.containsKey(dest))) {
                return -1;
            }

            Map<String, Integer> vertexDegree = calculateVertexDegrees();

            Deque<String> queue = new ArrayDeque<>();

            for (Map.Entry<String, Integer> entry : vertexDegree.entrySet()) {
                if (entry.getValue() == 0) {
                    queue.add(entry.getKey());
                }
            }

            assert !queue.isEmpty();

            Map<String, Integer> shortestPaths = new HashMap<>();
            boolean startPathFinding = false;

            while (!queue.isEmpty()) {

                String baseVertex = queue.poll();

                // start path tracking
                if (baseVertex.equals(src)) {
                    startPathFinding = true;
                    shortestPaths.put(baseVertex, 0);
                }
                // we are done with our shortest path search
                else if (baseVertex.equals(dest)) {
                    assert startPathFinding;
                    return shortestPaths.get(baseVertex);
                }

                for (EdgeWithWeight edge : adjList.get(baseVertex)) {

                    if (startPathFinding) {

                        int curPathWeight = shortestPaths.computeIfAbsent(edge.dest, key -> Integer.MAX_VALUE);
                        int newPathWeight = shortestPaths.get(baseVertex) + edge.weight;

                        if (newPathWeight < curPathWeight) {
                            shortestPaths.put(edge.dest, newPathWeight);
                        }
                    }

                    int curVertexDegree = vertexDegree.compute(edge.dest, (key, val) -> val - 1);

                    if (curVertexDegree == 0) {
                        queue.add(edge.dest);
                    }
                }
            }

            throw new IllegalStateException("shortest path not found between:  " + src + " and " + dest);
        }

        private Map<String, Integer> calculateVertexDegrees() {
            Map<String, Integer> vertexDegree = new HashMap<>();

            for (Map.Entry<String, List<EdgeWithWeight>> entry : adjList.entrySet()) {

                vertexDegree.putIfAbsent(entry.getKey(), 0);

                for (EdgeWithWeight edge : entry.getValue()) {
                    vertexDegree.compute(edge.dest, (key, val) -> val == null ? 1 : val + 1);
                }
            }

            return vertexDegree;
        }

        private Set<String> getSourceVertexes() {

            Set<String> candidates = new HashSet<>(adjList.keySet());

            for (List<EdgeWithWeight> allEdges : adjList.values()) {
                for (EdgeWithWeight edge : allEdges) {
                    candidates.remove(edge.dest);
                }
            }

            checkArgument(!candidates.isEmpty(), "No source vertex (with 0 in degree) in DAG.");

            return Collections.unmodifiableSet(candidates);
        }

        private List<EdgeWithWeight> getEdges(String vertex) {
            assert adjList.containsKey(vertex);
            return adjList.get(vertex);
        }

        private void addVertex(String vertex) {
            assert !adjList.containsKey(vertex);

            adjList.put(vertex, new ArrayList<>());
        }

        private void addEdge(String src, String dest, int weight) {
            adjList.get(src).add(new EdgeWithWeight(dest, weight));
        }

        private static final class EdgeWithWeight {
            final String dest;
            final int weight;

            EdgeWithWeight(String dest, int weight) {
                this.dest = dest;
                this.weight = weight;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                EdgeWithWeight that = (EdgeWithWeight) o;
                return weight == that.weight &&
                        Objects.equals(dest, that.dest);
            }

            @Override
            public int hashCode() {
                return Objects.hash(dest, weight);
            }
        }
    }

    private static final Random RAND = new Random();

    private static void randomPermutation(String[] arr) {
        for (int i = 0; i < arr.length - 1; ++i) {
            swap(arr, i, i + RAND.nextInt(arr.length - i));
        }
    }

    private static void swap(String[] arr, int from, int to) {
        assert arr != null;
        assert (from >= 0 && from < arr.length) && (to >= 0 && to < arr.length);

        String temp = arr[from];
        arr[from] = arr[to];
        arr[to] = temp;
    }

    private static String[] generateVertexesLabels(int length) {
        String[] labels = new String[length];

        final char firstCh = 'A';
        final char lastCh = 'Z';

        char baseLabel = firstCh;

        for (int i = 0; i < labels.length; ++i) {
            labels[i] = String.valueOf(baseLabel) + "-" + i;
            baseLabel = (baseLabel == lastCh) ? firstCh : (char) (baseLabel + 1);
        }

        randomPermutation(labels);
        return labels;
    }

    private AlgorithmsMain() {

        String[] labels = generateVertexesLabels(10_000);

        DirectAcyclicGraph graph = DirectAcyclicGraph.generate(labels);

        if (!graph.isConnected()) {
            throw new IllegalStateException("DAG not connected");
        }

        int pathWeight = graph.shortestPath(labels[0], labels[labels.length - 1]);

        LOG.info("pathWeight: " + pathWeight);

        LOG.info("AlgorithmsMain done...");
    }


    public static void main(String[] args) {
        try {
            new AlgorithmsMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
