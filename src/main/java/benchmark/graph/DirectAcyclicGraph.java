package benchmark.graph;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

final class DirectAcyclicGraph {

    private final Map<String, List<EdgeWithWeight>> adjList = new HashMap<>();

    boolean isConnected() {

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
     * Shortest path using Dijkstra algorithm.
     * <p>
     * time: O((V+E)*lgV)
     * space: O(V)
     */
    int shortestPathDijkstra(String src, String dest) {

        // check both vertexes present
        if (!(adjList.containsKey(src) && adjList.containsKey(dest))) {
            return -1;
        }

        Queue<EdgeWithWeight> minPathQueue = new PriorityQueue<>(EdgeWithWeight.WEIGHT_ASC_CMP);
        minPathQueue.add(new EdgeWithWeight(src, 0));

//        //TODO:
//        while (true) {
//
//            assert !minPathQueue.isEmpty();
//
//            EdgeWithWeight cur = minPathQueue.poll();
//
//            if (cur.dest.equals(dest)) {
//                return cur.weight;
//            }
//
//            for (EdgeWithWeight edge : adjList.get(cur.dest)) {
//
//            }
//        }

        return -1;
    }

    /**
     * Shortest path in DAG using topological sorting and left-to-right order of calculation.
     * <p>
     * time: O(V + E)
     * space: O(V + E)
     */
    int shortestPath(String src, String dest) {

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

        while (true) {

            assert !queue.isEmpty();

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

    Set<String> getSourceVertexes() {

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

    void addVertex(String vertex) {
        assert !adjList.containsKey(vertex);

        adjList.put(vertex, new ArrayList<>());
    }

    void addEdge(String src, String dest, int weight) {
        adjList.get(src).add(new EdgeWithWeight(dest, weight));
    }

}
