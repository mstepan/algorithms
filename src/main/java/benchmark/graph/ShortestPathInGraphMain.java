package benchmark.graph;


import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.Random;


public final class ShortestPathInGraphMain {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

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

    private ShortestPathInGraphMain() {

        for (int it = 0; it < 100; ++it) {
            String[] labels = generateVertexesLabels(10_000);

            DirectAcyclicGraph graph = DagGenerator.generate(labels);

            if (!graph.isConnected()) {
                throw new IllegalStateException("DAG not connected");
            }

            String src = labels[0];
            String dest = labels[labels.length - 1];

            int simpleShortestPath = graph.shortestPath(src, dest);
            int dijkstraShortestPath = graph.shortestPathDijkstra(src, dest);

            if( simpleShortestPath != dijkstraShortestPath ){
                throw new IllegalStateException("Paths aren't equals: simple = " + simpleShortestPath +
                        ", dijkstra = " + dijkstraShortestPath);
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
