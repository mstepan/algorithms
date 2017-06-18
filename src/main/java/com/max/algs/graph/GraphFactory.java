package com.max.algs.graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Graph factory.
 */
final class GraphFactory {

    private static final Random RAND = ThreadLocalRandom.current();

    private GraphFactory() {
    }

    /**
     * @param vertexesCount
     * @param edgeProbability - possible values  0% - 100%
     * @return
     */
    @SuppressWarnings("unused")
    public static Graph<Integer> createRandomGraph(int vertexesCount, int edgeProbability) {

        if (edgeProbability < 0 || edgeProbability > 100) {
            throw new IllegalArgumentException("'edgeProbability' is incorrect: " + edgeProbability + ", should be in range " +
                    "[1, 100]");
        }

        Graph<Integer> graph = Graph.createGraph();

        int skippedCount = 0;
        int addedCount = 0;
        int randValue;

        for (int ver1 = 0; ver1 < vertexesCount - 1; ver1++) {

            for (int ver2 = ver1 + 1; ver2 < vertexesCount; ver2++) {

                randValue = RAND.nextInt(100);

                if (randValue < edgeProbability) {
                    graph.addEdge(ver1, ver2);
                    ++addedCount;
                }
                else {
                    ++skippedCount;
                }
            }
        }

        return graph;
    }

    public static Graph<String> createGraph(Path path) {

        if (!Files.exists(path)) {
            throw new IllegalArgumentException("Can't create graph from file: '" + path + "'");
        }

        Graph<String> graph = new Graph<>(false);


        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] edgeData = line.split("-");
                graph.addEdge(edgeData[0], edgeData[1]);

            }
        }
        catch (IOException ioEx) {
            throw new IllegalStateException("");
        }

        return graph;
    }

    public static Graph<String> createChessboardGraph(int boardSize) {

        Graph<String> graph = Graph.createGraph();

        for (int row = 0; row < boardSize; row++) {

            for (int col = 0; col < boardSize; col++) {

                String baseVer = row + "_" + col;

                graph.addVertex(baseVer);

                if (row - 1 >= 0 && col - 1 >= 0) {
                    graph.addEdge(baseVer, (row - 1) + "_" + (col - 1));
                }

                if (row - 1 >= 0 && col + 1 < boardSize) {
                    graph.addEdge(baseVer, (row - 1) + "_" + (col + 1));
                }
            }
        }

        return graph;
    }
}
