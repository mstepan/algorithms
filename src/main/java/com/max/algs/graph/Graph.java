package com.max.algs.graph;

import com.max.algs.ds.set.DisjointSet;
import com.max.algs.it.SubsetIterator;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * Undirected unweighted graph represented as an adjacency list
 */
public class Graph<T> {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private static final Random RAND = new Random();


    private final Map<T, Map<T, Edge<T>>> vertexes = new TreeMap<>();
    private final boolean directed;
    private int edgesCount;

    /**
     * Graph copy constructor.
     */
    public Graph(Graph<T> other) {
        this(other.directed);

        for (Map.Entry<T, Map<T, Edge<T>>> entry : other.vertexes.entrySet()) {

            Map<T, Edge<T>> edgesMap = entry.getValue();

            Map<T, Edge<T>> edgesMapCopy = new HashMap<>();
            for (Map.Entry<T, Edge<T>> edgeEntry : edgesMap.entrySet()) {
                edgesMapCopy.put(edgeEntry.getKey(), new Edge<>(edgeEntry.getValue()));
            }

            vertexes.put(entry.getKey(), edgesMapCopy);
        }
    }

    Graph(boolean directed) {
        this.directed = directed;
    }

    public static <T> Graph<T> createGraph() {
        return new Graph<T>(false);
    }

    public static <T> Graph<T> createDirectedGraph() {
        return new Graph<T>(true);
    }

    /**
     * Find all connected components with all vertexes of 'k' degree.
     * See: https://en.wikipedia.org/wiki/Degeneracy_%28graph_theory%29
     * <p>
     * time: O(V+E)
     * space: O(V)
     */
    public List<Set<T>> findKNodes(int k) {
        checkArgument(k >= 0, "Can't find negative k-nodes: k = %s", k);

        List<Set<T>> result = new ArrayList<>();

        Set<T> deleted = new HashSet<>();

        Queue<T> candidatesToDelete = new ArrayDeque<>();
        Map<T, Integer> vertexesDegree = new HashMap<>();

        for (T vertex : getVertexes()) {
            int curDegree = getAdjVertexes(vertex).size();
            vertexesDegree.put(vertex, curDegree);

            if (curDegree < k) {
                candidatesToDelete.add(vertex);
            }
        }

        while (!candidatesToDelete.isEmpty()) {
            T ver = candidatesToDelete.poll();

            deleted.add(ver);

            for (T adjVer : getAdjVertexes(ver)) {

                if (!deleted.contains(adjVer)) {
                    int adjDegree = vertexesDegree.get(adjVer);
                    --adjDegree;

                    vertexesDegree.put(adjVer, adjDegree);

                    if (adjDegree < k) {
                        candidatesToDelete.add(adjVer);
                    }
                }
            }
        }

        Set<T> visited = new HashSet<>();

        for (T ver : getVertexes()) {

            if (!visited.contains(ver)) {

                Set<T> singleConnectedComponent = new LinkedHashSet<>();

                Set<T> marked = new HashSet<>();

                Queue<T> bfsQueue = new ArrayDeque<>();
                bfsQueue.add(ver);

                while (!bfsQueue.isEmpty()) {

                    T curVer = bfsQueue.poll();
                    visited.add(curVer);

                    if (!deleted.contains(curVer)) {
                        singleConnectedComponent.add(curVer);
                    }

                    for (T adjVer : getAdjVertexes(curVer)) {
                        if (!(marked.contains(adjVer) || visited.contains(adjVer))) {
                            marked.add(adjVer);
                            bfsQueue.add(adjVer);
                        }
                    }
                }

                result.add(singleConnectedComponent);
            }
        }

        return result;
    }

    public boolean isDirected() {
        return directed;
    }

    public boolean isUndirected() {
        return !directed;
    }

    /**
     * Check if graph is bipartite.
     * <p>
     * time: O(V+E)
     * space: O(V)
     */
    public boolean isBipartite() {

        if (isEmpty()) {
            return true;
        }

        T basVer = firstVertex();

        Queue<T> queue = new ArrayDeque<>();
        Map<T, BiColor> marked = new HashMap<>();

        queue.add(basVer);
        marked.put(basVer, BiColor.WHITE);

        while (!queue.isEmpty()) {

            T ver = queue.poll();
            BiColor curColor = marked.get(ver);

            for (T adjVer : getAdjVertexes(ver)) {

                BiColor adjColor = marked.get(adjVer);

                if (adjColor == null) {
                    queue.add(adjVer);
                    marked.put(adjVer, curColor.next());
                }
                else if (adjColor == curColor) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Floyd-Warshall algorithm for all pairs shortest path.
     * <p>
     * N - vertexes count
     * time: O(N^3)
     * space: O(N^2)
     *
     * @return
     */
    public Map<T, Map<T, Integer>> allPairsShortestPath() {

        Map<T, Map<T, Integer>> arr = new TreeMap<>();

        for (T ver : vertexes.keySet()) {

            Map<T, Integer> row = new TreeMap<>();

            for (T otherVer : vertexes.keySet()) {

                if (ver == otherVer) {
                    row.put(otherVer, 0);
                }
                else {
                    Edge<T> edge = getEdge(ver, otherVer);
                    row.put(otherVer, edge != null ? edge.weight : Integer.MAX_VALUE);
                }
            }

            arr.put(ver, row);
        }

        for (T ver : vertexes.keySet()) {

            for (T row : vertexes.keySet()) {
                for (T col : vertexes.keySet()) {

                    if (!row.equals(col) && !row.equals(ver) && !col.equals(ver)) {

                        int minValue = arr.get(row).get(col);

                        if (arr.get(row).get(ver) != Integer.MAX_VALUE &&
                                arr.get(ver).get(col) != Integer.MAX_VALUE) {
                            int minThroughVer = arr.get(row).get(ver) + arr.get(ver).get(col);

                            if (minThroughVer < minValue) {
                                arr.get(row).put(col, minThroughVer);
                            }
                        }

                    }

                }
            }

        }


        return arr;
    }

    /**
     * Returns all simple path (path without repetitions) from 'src' to 'dest'.
     * Use backtracking technique.
     */
    public List<String> findAllPaths(T src, T dest) {

        if (!(hasVertex(src) && hasVertex(dest))) {
            return new ArrayList<>();
        }

        List<String> res = new ArrayList<>();

        Set<T> used = new HashSet<>();
        Deque<T> path = new ArrayDeque<>();

        collectPathsRec(src, dest, used, path, res);

        return res;

    }

    private void collectPathsRec(T cur, T dest, Set<T> used, Deque<T> path, List<String> res) {

        if (cur.equals(dest)) {
            res.add(constructPath(path, dest));
            return;
        }

        used.add(cur);
        path.push(cur);

        getAdjVertexes(cur).forEach(adj -> {
            if (!used.contains(adj)) {
                collectPathsRec(adj, dest, used, path, res);
            }
        });

        path.pop();
        used.remove(cur);
    }

    private String constructPath(Deque<T> path, T dest) {

        StringBuilder buf = new StringBuilder();
        buf.append(dest);

        path.forEach(value -> buf.append("-").append(value));

        buf.reverse();

        return buf.toString();
    }

    private boolean hasVertex(T vertex) {
        return vertexes.containsKey(vertex);
    }

    public List<Edge<T>> getAllEdges() {
        List<Edge<T>> edges = new ArrayList<>();

        for (Map.Entry<T, Map<T, Edge<T>>> entry : vertexes.entrySet()) {
            edges.addAll(entry.getValue().values());
        }

        return edges;
    }

    public boolean isEmpty() {
        return vertexes.isEmpty();
    }

    public List<T> vertexCoverageApproximation() {

        Set<Edge<T>> deletedEdges = new HashSet<>();

        List<T> coverage = new ArrayList<>();

        for (Map.Entry<T, Map<T, Edge<T>>> entry : vertexes.entrySet()) {

            for (Map.Entry<T, Edge<T>> edgeEntry : entry.getValue().entrySet()) {

                if (!deletedEdges.contains(edgeEntry.getValue())) {

                    T vertex1 = entry.getKey();
                    T vertex2 = edgeEntry.getKey();

                    coverage.add(vertex1);
                    coverage.add(vertex2);

                    markAllIncidentEdgesAsDeleted(vertex1, deletedEdges);
                    markAllIncidentEdgesAsDeleted(vertex2, deletedEdges);
                    break;
                }
            }
        }
        return coverage;
    }

    private void markAllIncidentEdgesAsDeleted(T vertex, Set<Edge<T>> deletedEdges) {

        for (Map.Entry<T, Edge<T>> edgeEntry : vertexes.get(vertex).entrySet()) {

            deletedEdges.add(edgeEntry.getValue());

            for (Map.Entry<T, Edge<T>> otherSideEdgeEntry : vertexes.get(edgeEntry.getKey()).entrySet()) {
                if (otherSideEdgeEntry.getKey().equals(vertex)) {
                    deletedEdges.add(otherSideEdgeEntry.getValue());
                    break;
                }
            }

        }

    }

    /**
     * Get all minimal vertex coverage using brute force algorithm.
     */
    public List<T> vertexCoverageBruteforce() {

        Set<T> allVertexes = vertexes.keySet();

        SubsetIterator<T> subsetIt = new SubsetIterator<>(allVertexes);

        List<T> min = null;

        while (subsetIt.hasNext()) {
            T[] subset = subsetIt.next();

            if (isVertexCoverage(subset)) {

                if (min == null || subset.length < min.size()) {
                    min = Arrays.asList(subset);
                }
            }
        }


        return min;
    }

    private boolean isVertexCoverage(T[] set) {

        Set<Edge<T>> coveredEdges = new HashSet<>();

        for (T vertex : set) {

            Collection<Edge<T>> edges = vertexes.get(vertex).values();

            for (Edge<T> singleEdge : edges) {

                coveredEdges.add(singleEdge);

                T destVertex = singleEdge.dest;

                coveredEdges.add(vertexes.get(destVertex).get(vertex));

            }
        }

        return coveredEdges.size() == edgesCount;
    }

    /**
     * Construct minimum spanning tree using greedy approach (Kruskal's algorithm).
     * <p>
     * V - vertexes count.
     * E - edges count.
     * <p>
     * time: O(E*lgE)
     * space: O(V+E)
     */
    public List<Edge<T>> minSpanningTree() {

        DisjointSet<T> disjoinSet = new DisjointSet<>();
        getVertexes().forEach(disjoinSet::add);

        List<Edge<T>> sortedEdges = getAllEdges();
        sortedEdges.sort(Edge.WEIGHT_ASC_CMP);

        List<Edge<T>> spanningTree = new ArrayList<>();

        sortedEdges.forEach(edge -> {
            if (disjoinSet.find(edge.src) != disjoinSet.find(edge.dest)) {
                spanningTree.add(edge);
                disjoinSet.union(edge.src, edge.dest);
            }
        });

        return spanningTree;
    }

    public List<T> bfs() {

        if (isEmpty()) {
            return new ArrayList<>();
        }

        List<T> bfsPath = new ArrayList<>();

        T firstVertex = vertexes.keySet().iterator().next();

        Deque<T> deque = new ArrayDeque<>();
        deque.add(firstVertex);

        Set<T> handled = new HashSet<>();
        Set<T> marked = new HashSet<>();
        marked.add(firstVertex);


        while (!deque.isEmpty()) {
            T vertex = deque.poll();

            bfsPath.add(vertex);

            Collection<Edge<T>> edges = vertexes.get(vertex).values();

            for (Edge<T> edge : edges) {
                if (!marked.contains(edge.dest) && !handled.contains(edge.dest)) {
                    deque.add(edge.dest);
                    marked.add(edge.dest);
                }
            }


            marked.remove(firstVertex);
            handled.add(firstVertex);
        }


        return bfsPath;
    }

    public Map<T, Integer> bellmanFordShortestPath() {

        Map<T, Integer> distance = new TreeMap<>();

        Iterator<T> vertexIt = vertexes.keySet().iterator();

        T srcVertex = vertexIt.next();

        distance.put(srcVertex, 0);

        while (vertexIt.hasNext()) {
            distance.put(vertexIt.next(), Integer.MAX_VALUE);
        }

        // calculate relaxation for each vertex, edge times

        for (int i = 0; i < vertexes.size() - 1; i++) {
            for (Map<T, Edge<T>> edgeEntry : vertexes.values()) {

                for (Edge<T> edge : edgeEntry.values()) {
                    int uDist = distance.get(edge.src);
                    int newDistance = uDist + edge.weight;

                    int vDist = distance.get(edge.dest);

                    if (newDistance < vDist) {
                        distance.put(edge.dest, newDistance);
                    }
                }
            }
        }

        // check for negative cycles
        for (Map<T, Edge<T>> edgeEntry : vertexes.values()) {
            for (Edge<T> edge : edgeEntry.values()) {

                int uDist = distance.get(edge.src);
                int vDist = distance.get(edge.dest);

                if (vDist > uDist + edge.weight) {
                    throw new IllegalStateException("Negative cycle detected in graph");
                }
            }
        }


        return distance;
    }

    public T firstVertex() {
        if (isEmpty()) {
            throw new IllegalStateException("Graph is empty");
        }
        return vertexes.keySet().iterator().next();
    }

    public List<T> getVertexes() {
        return new ArrayList<>(vertexes.keySet());
    }

    /**
     * Do BFS and find path.
     */
    public List<T> shortestPath(T src, T dest) {

        if (!vertexes.containsKey(src) || !vertexes.containsKey(dest)) {
            return new ArrayList<>();
        }

        Set<T> visited = new HashSet<>();
        Set<T> marked = new HashSet<>();

        Map<T, T> parent = new HashMap<>();

        Deque<T> queue = new ArrayDeque<>();
        queue.add(src);
        marked.add(src);

        while (!queue.isEmpty()) {

            T ver = queue.poll();

            if (ver.equals(dest)) {
                return reconstructPath(src, dest, parent);
            }

            visited.add(ver);
            marked.remove(ver);

            Collection<Edge<T>> adjVertexes = vertexes.get(ver).values();

            for (Edge<T> adj : adjVertexes) {
                if (!visited.contains(adj) && !marked.contains(adj)) {
                    queue.add(adj.dest);
                    marked.add(adj.dest);

                    parent.put(adj.dest, ver);
                }
            }

        }

        return new ArrayList<>();
    }


    private List<T> reconstructPath(T src, T dest, Map<T, T> parent) {


        T cur = dest;
        List<T> path = new ArrayList<>();

        while (!cur.equals(src)) {
            path.add(cur);
            cur = parent.get(cur);
        }

        path.add(src);

        Collections.reverse(path);

        return path;
    }


    public Set<T> getAdjVertexes(T ver) {

        Collection<Edge<T>> neighbourEdges = vertexes.get(ver).values();

        if (neighbourEdges == null) {
            throw new IllegalArgumentException("Vertex not found: '" + String.valueOf(ver) + "'");
        }

        Set<T> nighbour = new HashSet<>();

        for (Edge<T> edge : neighbourEdges) {
            nighbour.add(edge.dest);
        }

        return nighbour;
    }


    public boolean addVertex(T ver) {
        return vertexes.put(ver, new TreeMap<T, Edge<T>>()) == null;
    }

    /**
     * Remove vertex and all related edges.
     */
    public void removeVertex(T ver) {
        for (T adjVer : getAdjVertexes(ver)) {
            removeEdge(ver, adjVer);
        }
        vertexes.remove(ver);
    }

    public boolean addEdge(T ver1, T ver2, int weight) {
        Map<T, Edge<T>> neighbour1 = vertexes.get(ver1);

        if (neighbour1 == null) {
            neighbour1 = new TreeMap<>();
            vertexes.put(ver1, neighbour1);
        }

        Map<T, Edge<T>> neighbour2 = vertexes.get(ver2);

        if (neighbour2 == null) {
            neighbour2 = new TreeMap<>();
            vertexes.put(ver2, neighbour2);
        }

        if (!directed) {
            neighbour2.put(ver1, new Edge<>(ver2, ver1, weight));
            ++edgesCount;
        }


        ++edgesCount;

        neighbour1.put(ver2, new Edge<>(ver1, ver2, weight));

        return true;
    }

    public boolean addEdge(T ver1, T ver2) {
        return addEdge(ver1, ver2, 0);
    }

    /**
     * Remove edge 'src-dest' from graph.
     */
    public boolean removeEdge(T src, T dest) {

        checkArgument(vertexes.containsKey(src), "Vertex '%s' doesn't belong a graph", src);
        checkArgument(vertexes.containsKey(dest), "Vertex '%s' doesn't belong a graph", dest);

        Map<T, Edge<T>> neighbour = vertexes.get(src);

        boolean edgeWasRemoved = neighbour.remove(dest) != null;

        // if edge was removed and graph is 'undirected', remove edge from another side
        if (edgeWasRemoved && !directed) {
            vertexes.get(dest).remove(src);
        }

        return edgeWasRemoved;
    }


    public boolean hasEdge(T ver1, T ver2) {

        Map<T, Edge<T>> neighbour = vertexes.get(ver1);

        if (neighbour == null) {
            return false;
        }

        return neighbour.containsKey(ver2);
    }

    private Edge<T> getEdge(T ver1, T ver2) {

        Map<T, Edge<T>> neighbour = vertexes.get(ver1);

        if (neighbour == null) {
            return null;
        }

        return neighbour.get(ver2);
    }


    public Map<T, Integer> colorGraph() {

        final int lastColorIndex = vertexes.size();

        Map<T, Integer> vertexColors = new HashMap<>();

        // handle each connected component
        for (Map.Entry<T, Map<T, Edge<T>>> vertexEntry : vertexes.entrySet()) {

            T firstVertex = vertexEntry.getKey();

            // separate connected component found
            if (!vertexColors.containsKey(firstVertex)) {

                Set<T> marked = new HashSet<>();

                Queue<T> queue = new ArrayDeque<>();
                queue.add(firstVertex);

                BitSet colorsInUse = new BitSet(lastColorIndex);

                while (!queue.isEmpty()) {

                    T curVertex = queue.poll();
                    colorsInUse.clear(0, lastColorIndex);

                    for (T adjVer : getAdjVertexes(curVertex)) {

                        Integer adjColor = vertexColors.get(adjVer);

                        if (adjColor != null) {
                            colorsInUse.set(adjColor);
                        }
                        else if (!marked.contains(adjVer)) {
                            queue.add(adjVer);
                            marked.add(adjVer);
                        }
                    }

                    vertexColors.put(curVertex, colorsInUse.nextClearBit(0));
                    marked.remove(curVertex);
                }
            }
        }

        return vertexColors;
    }


    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();

        buf.append(LINE_SEPARATOR);

        vertexes.keySet();

        for (T ver : vertexes.keySet()) {

            buf.append(ver + " => ");

            Map<T, Edge<T>> edges = vertexes.get(ver);


            for (Map.Entry<T, Edge<T>> edge : edges.entrySet()) {

                if (edge.getValue().weight == 0) {
                    buf.append(edge.getValue().dest).append(", ");
                }
                else {
                    buf.append("(").append(edge.getValue().dest).append(", ").append(edge.getValue().weight).append(") ");
                }
            }

            buf.append(LINE_SEPARATOR);
        }

        return buf.toString();
    }


}
