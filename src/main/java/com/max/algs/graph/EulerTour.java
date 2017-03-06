package com.max.algs.graph;

import com.google.common.base.Optional;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Find Euler tour if exists. See: https://en.wikipedia.org/wiki/Eulerian_path
 * <p>
 * 1. Start with an empty stack and an empty circuit (eulerian path).
 * - If all vertices have even degree - choose any of them.
 * - If there are exactly 2 vertices having an odd degree - choose one of them.
 * - Otherwise no euler circuit or path exists.
 * <p>
 * 2. If current vertex has no neighbors - add it to circuit, remove the last vertex
 * from the stack and set it as the current one.
 * Otherwise (in case it has neighbors) - add the vertex to the stack, take any of its neighbors,
 * remove the edge between selected neighbor and that vertex, and set that neighbor as the current vertex.
 * <p>
 * Repeat step 2 until the current vertex has no more neighbors and the stack is empty.
 */
public final class EulerTour {


    public static <T> List<T> buildTour(Graph<T> originalGraph) {

        checkArgument(originalGraph != null, "null 'graph' passed");

        Optional<T> preconditions = checkPreconditions(originalGraph);

        if (!preconditions.isPresent()) {
            return Collections.emptyList();
        }

        Graph<T> graphCopy = new Graph<T>(originalGraph);
        List<T> tour = new ArrayList<>();

        Deque<T> stack = new ArrayDeque<>();
        T curVer = preconditions.get();

        while (curVer != null) {

            Set<T> adjVertexes = graphCopy.getAdjVertexes(curVer);

            if( adjVertexes.isEmpty() ){
                tour.add(curVer);

                curVer = null;

                if( ! stack.isEmpty() ){
                    curVer = stack.pop();
                }
            }
            else {
                stack.push(curVer);
                T nextVer = adjVertexes.iterator().next();
                graphCopy.removeEdge(curVer, nextVer);
                curVer = nextVer;
            }
        }
        return tour;
    }

    private static <T> Optional<T> checkPreconditions(Graph<T> graph) {

        // graph can't contain Euler tour, if more then 2 connected components
        final int connectedComponentsCount = GraphUtils.connectedComponents(graph).size();

        if (connectedComponentsCount > 1) {
            return Optional.absent();
        }

        List<T> vertexes = graph.getVertexes();

        int oddVertexesCount = 0;

        T curVertex = null;

        for (T singleVertex : vertexes) {

            if (curVertex == null) {
                curVertex = singleVertex;
            }

            Set<T> adjVertexes = graph.getAdjVertexes(singleVertex);

            // odd in degree
            if ((adjVertexes.size() & 1) != 0) {
                curVertex = singleVertex;
                ++oddVertexesCount;
            }
        }

        // graph can't contain Euler tour if odd vertexes count != 0 or != 2
        if (oddVertexesCount != 0 && oddVertexesCount != 2) {
            return Optional.absent();
        }

        return Optional.fromNullable(curVertex);
    }


}
