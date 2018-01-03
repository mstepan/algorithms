package benchmark.graph;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

final class FloydWarshallAllPairsPath {

    private FloydWarshallAllPairsPath() {
        throw new AssertionError("Can't instantiate utility only class");
    }


    /**
     * All pairs shortest path using Floyd-Warshall.
     */
    static int calculateShortestPath(String src, String dest,
                                     Map<String, List<EdgeWithWeight>> adjList) {

        String[] vertexes = vertexesArr(adjList);

        int[][] allPaths = initialPathsArray(vertexes, adjList);

        for (int m = 0; m < vertexes.length; ++m) {

            for (int i = 0; i < vertexes.length; ++i) {
                for (int j = 0; j < vertexes.length; ++j) {

                    if (allPaths[i][m] != Integer.MAX_VALUE && allPaths[m][j] != Integer.MAX_VALUE) {
                        int newPath = allPaths[i][m] + allPaths[m][j];

                        if (newPath < allPaths[i][j]) {
                            allPaths[i][j] = newPath;
                        }
                    }
                }
            }
        }

        int srcIndex = 0;
        int destIndex = 0;

        for (int i = 0; i < vertexes.length; ++i) {
            if (vertexes[i].equals(src)) {
                srcIndex = i;
            }
            else if (vertexes[i].equals(dest)) {
                destIndex = i;
            }
        }

        return allPaths[srcIndex][destIndex];
    }

    private static int[][] initialPathsArray(String[] vertexes, Map<String, List<EdgeWithWeight>> adjList) {
        int[][] allPaths = new int[vertexes.length][vertexes.length];

        for (int i = 0; i < allPaths.length; ++i) {
            Arrays.fill(allPaths[i], Integer.MAX_VALUE);
        }

        for (int i = 0; i < vertexes.length; ++i) {
            for (int j = 0; j < vertexes.length; ++j) {
                if (i == j) {
                    allPaths[i][j] = 0;
                }
                else {
                    EdgeWithWeight edge = getEdgeWeight(vertexes[i], vertexes[j], adjList);

                    if (edge != null) {
                        allPaths[i][j] = edge.weight;
                    }
                }
            }
        }

        return allPaths;
    }

    private static EdgeWithWeight getEdgeWeight(String src, String dest,
                                                Map<String, List<EdgeWithWeight>> adjList) {

        List<EdgeWithWeight> edges = adjList.get(src);

        for (EdgeWithWeight singleEdge : edges) {
            if (singleEdge.dest.equals(dest)) {
                return singleEdge;
            }
        }

        return null;
    }


    private static String[] vertexesArr(Map<String, List<EdgeWithWeight>> adjList) {
        String[] vertexes = new String[adjList.size()];

        Iterator<String> it = adjList.keySet().iterator();

        for (int i = 0; i < vertexes.length; ++i) {
            assert it.hasNext();
            vertexes[i] = it.next();
        }

        return vertexes;
    }


}
