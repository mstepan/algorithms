package com.max.algs;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

public final class AlgorithmsMain {

    private static final class Location {
        final int x;
        final int y;

        Location(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || obj.getClass() != getClass()) {
                return false;
            }

            Location other = (Location) obj;

            return x == other.x && y == other.y;
        }

        @Override
        public String toString() {
            return "(" + x + "; " + y + ")";
        }
    }

    private static final class Dimensions {
        final int rows;
        final int cols;

        Dimensions(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;

            checkArgument(this.rows >= 0);
            checkArgument(this.cols >= 0);
        }

        @Override
        public String toString() {
            return "rows: " + rows + ", cols: " + cols;
        }

        boolean isInRange(int row, int col) {
            return row >= 0 && row < rows && col >= 0 && col < cols;
        }
    }

    /**
     * Use BFS to find shorted path in undirected graph.
     */
    private List<Location> findShortestPath(Location src, Location dest, Dimensions dimension,
                                            Set<Location> obstacles, MoveDirections moveDirections) {

        assert src != null : "src is null";
        assert dest != null : "src is null";
        assert dimension != null : "dimension is null";
        assert obstacles != null : "obstacles is null";
        assert moveDirections != null : "moveDirections is null";

        if (src.equals(dest)) {
            return Collections.singletonList(src);
        }

        Map<Location, Location> markedWithParents = new HashMap<>();
        markedWithParents.put(src, null);

        Deque<Location> queue = new ArrayDeque<>();
        queue.push(src);

        while (!queue.isEmpty()) {
            Location cur = queue.poll();

            if (dest.equals(cur)) {
                break;
            }

            List<Location> nextPositions = generateValidPositions(cur, dimension, moveDirections).stream().
                    filter(pos -> !obstacles.contains(pos) && !markedWithParents.containsKey(pos)).
                    collect(Collectors.toList());

            for (Location pos : nextPositions) {
                assert !markedWithParents.containsKey(pos) : "incorrect location detected, already marked";
                assert !obstacles.contains(pos) : "incorrect location detected, obstacles found";
                assert pos.x >= 0 && pos.x < dimension.rows : "out of range location value for row";
                assert pos.y >= 0 && pos.y < dimension.cols : "out of range location value for column";

                queue.add(pos);
                markedWithParents.put(pos, cur);
            }
        }

        // there is no path from 'src' to 'dest'
        if (!markedWithParents.containsKey(dest)) {
            return Collections.emptyList();
        }

        assert markedWithParents.containsKey(src) && markedWithParents.containsKey(dest) :
                "src or dest location is not marked as visited";

        return reconstructPath(dest, markedWithParents);
    }

    private List<Location> reconstructPath(Location dest, Map<Location, Location> markedWithParents) {

        Location cur = dest;

        LinkedList<Location> path = new LinkedList<>();

        int it = Integer.MAX_VALUE;

        while (cur != null) {
            path.addFirst(cur);
            cur = markedWithParents.get(cur);
            --it;

            if (it == 0) {
                throw new IllegalStateException("Too many iterations for path reconstruction, probably infinity loop");
            }
        }

        assert !path.isEmpty() : "empty path reconstructed";

        return path;
    }

    private enum MoveDirections {
        STANDARD(new int[][]{
                {1, 0}, {-1, 0},
                {0, 1}, {0, -1}
        }),
        VERTICAL(new int[][]{
                {-1, -1}, {1, 1},
                {-1, 1}, {1, -1}
        }
        ),
        ALL(combineArrays(STANDARD.getOffsets(), VERTICAL.getOffsets()));

        MoveDirections(int[][] offsets) {
            this.offsets = offsets;
        }

        private final int[][] offsets;

        public int[][] getOffsets() {
            return offsets;
        }

        private static int[][] combineArrays(int[][] first, int[][] second) {
            int[][] combined = new int[first.length + second.length][];

            int index = 0;

            for (int i = 0; i < first.length; ++i, ++index) {
                combined[index] = first[i];
            }

            for (int i = 0; i < second.length; ++i, ++index) {
                combined[index] = second[i];
            }

            return combined;
        }
    }

    private List<Location> generateValidPositions(Location cur, Dimensions dimension, MoveDirections direction) {

        List<Location> locations = new ArrayList<>();

        for (int[] offset : direction.getOffsets()) {
            int x = cur.x + offset[0];
            int y = cur.y + offset[1];

            if (dimension.isInRange(x, y)) {
                locations.add(new Location(x, y));
            }
        }

        return locations;
    }

    private AlgorithmsMain() {

        Set<Location> obstacles = new HashSet<>();
        obstacles.add(new Location(3, 2));
        obstacles.add(new Location(6, 6));
        obstacles.add(new Location(7, 0));
        obstacles.add(new Location(2, 8));
        obstacles.add(new Location(5, 9));
        obstacles.add(new Location(8, 4));
        obstacles.add(new Location(2, 4));
        obstacles.add(new Location(0, 8));
        obstacles.add(new Location(1, 3));
        obstacles.add(new Location(6, 3));
        obstacles.add(new Location(9, 3));
        obstacles.add(new Location(1, 9));
        obstacles.add(new Location(3, 0));
        obstacles.add(new Location(3, 7));
        obstacles.add(new Location(4, 2));
        obstacles.add(new Location(7, 8));
        obstacles.add(new Location(2, 2));
        obstacles.add(new Location(4, 5));
        obstacles.add(new Location(5, 6));
        obstacles.add(new Location(10, 2));
        obstacles.add(new Location(6, 2));
        obstacles.add(new Location(6, 10));
        obstacles.add(new Location(4, 0));
        obstacles.add(new Location(7, 5));
        obstacles.add(new Location(7, 9));
        obstacles.add(new Location(8, 1));
        obstacles.add(new Location(5, 7));
        obstacles.add(new Location(4, 4));
        obstacles.add(new Location(8, 7));
        obstacles.add(new Location(9, 2));
        obstacles.add(new Location(10, 9));
        obstacles.add(new Location(2, 6));

        List<Location> path = findShortestPath(new Location(0, 0), new Location(10, 10), new Dimensions(11, 11), obstacles,
                                               MoveDirections.STANDARD);
        /*
        [(0; 0), (0; 1), (0; 2), (0; 3), (0; 4), (1; 4), (1; 5), (2; 5), (3; 5), (3; 6), (4; 6), (4; 7), (4; 8), (5; 8), (6;
        8), (6; 7), (7; 7), (7; 6), (8; 6), (9; 6), (9; 7), (9; 8), (9; 9), (9; 10), (10; 10)]
        [(0; 0), (0; 1), (0; 2), (0; 3), (0; 4), (0; 5), (1; 5), (2; 5), (3; 5), (3; 6), (4; 6), (4; 7), (4; 8), (5; 8), (6;
        8), (6; 7), (7; 7), (7; 6), (8; 6), (8; 5), (9; 5), (10; 5), (10; 6), (10; 7), (10; 8), (9; 8), (9; 9), (9; 10), (10;
         10)]
         */

        System.out.println(path);
    }


    public static void main(String[] args) {
        try {
            new AlgorithmsMain();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
