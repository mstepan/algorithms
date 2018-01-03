package benchmark.graph;


import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;


public final class ShortestPathInGraphMain {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private ShortestPathInGraphMain() {

        for (int it = 0; it < 10; ++it) {
            String[] labels = ShortestPathInGraphBenchmark.generateVertexesLabels(1000);

            DirectAcyclicGraph graph = DagGenerator.generate(labels);

            if (!graph.isConnected()) {
                throw new IllegalStateException("DAG not connected");
            }

            String src = labels[0];
            String dest = labels[labels.length - 1];

            int simpleShortestPath = graph.shortestPath(src, dest);
            int dijkstraShortestPath = graph.shortestPathDijkstra(src, dest);
            int allPairsShortestPath = graph.shortestPathAllPairs(src, dest);

            if (simpleShortestPath != dijkstraShortestPath || simpleShortestPath != allPairsShortestPath) {
                throw new IllegalStateException("Paths aren't equals: simple = " + simpleShortestPath +
                        ", dijkstra = " + dijkstraShortestPath + ", allPairs: " + allPairsShortestPath);
            }
        }

//        String src = "A";
//        String dest = "E";
//
//        DirectAcyclicGraph graph = new DirectAcyclicGraph();
//
//        graph.addVertex("A");
//        graph.addVertex("B");
//        graph.addVertex("C");
//        graph.addVertex("D");
//        graph.addVertex("E");
//
//        graph.addEdge("A", "B", 1);
//        graph.addEdge("A", "C", 2);
//        graph.addEdge("B", "D", 5);
//        graph.addEdge("C", "D", 1);
//        graph.addEdge("D", "E", 1);
//        graph.addEdge("C", "E", 8);


        LOG.info("ShortestPathInGraphMain done...");
    }

    public static void main(String[] args) {
        try {
            new ShortestPathInGraphMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
