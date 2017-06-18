package com.max.algs.graph;

import java.util.*;

final class HamiltonianCycle<T> {

    private final Graph<T> graph;
    private final List<List<T>> allCycles = new ArrayList<List<T>>();
    private final Set<T> inUse = new HashSet<>();
    private final List<T> allVertexes;

    HamiltonianCycle(Graph<T> graph) {
        super();
        if (graph == null) {
            throw new IllegalArgumentException("NULL 'graph' passed");
        }
        this.graph = graph;
        this.allVertexes = Collections.unmodifiableList(graph.getVertexes());
    }

    List<List<T>> findCycles() {

        if (graph.isEmpty()) {
            return new ArrayList<>();
        }

        allCycles.clear();
        inUse.clear();

        T ver = allVertexes.get(0);

        Deque<T> res = new ArrayDeque<>();

        inUse.add(ver);
        res.addLast(ver);

        hamiltonCycleRec(ver, res);

        return allCycles;
    }


    private void hamiltonCycleRec(T ver, Deque<T> res) {

        assert res.size() <= allVertexes.size();

        if (res.size() == allVertexes.size()) {
            if (graph.hasEdge(ver, res.getFirst())) {
                res.addLast(res.getFirst());
                allCycles.add(new ArrayList<>(res));
                res.removeLast();
            }
            return;
        }


        for (T otherVer : graph.getAdjVertexes(ver)) {

            if (!inUse.contains(otherVer)) {

                inUse.add(otherVer);
                res.addLast(otherVer);

                hamiltonCycleRec(otherVer, res);

                res.removeLast();
                inUse.remove(otherVer);
            }

        }
    }
}
