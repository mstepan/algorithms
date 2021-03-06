package com.max.algs;

import com.max.algs.util.DoubleUtils;
import org.apache.log4j.Logger;

import java.util.*;


/**
 * https://uva.onlinejudge.org/external/2/225.html
 */
final class GolygonFinder {

    private static final Logger LOG = Logger.getLogger(GolygonFinder.class);

    private GolygonFinder() {

        List<XYPair> blocks = new ArrayList<>();
        blocks.add(new XYPair(-2, 0));
        blocks.add(new XYPair(6, -2));

        findPath(blocks);

        LOG.info("Main done...");
    }

    public static void main(String[] args) {
        new GolygonFinder();
    }

    void findPath(List<XYPair> blocks) {
        final int startPathLength = 8;
        findPathRec(XYPair.ZERO, blocks, new ArrayDeque<>(), startPathLength);
    }

    void findPathRec(XYPair curPoint, List<XYPair> blocks, Deque<Direction> result, int pathLength) {

        if (pathLength == 0) {
            if (XYPair.ZERO.equals(curPoint)) {
                LOG.info("Found golygon: " + decodeInReverseOrder(result));
            }

            return;
        }

        for (Direction singleDirection : Direction.values()) {
            tryMoveInDirection(curPoint, blocks, singleDirection, result, pathLength);
        }
    }

    String decodeInReverseOrder(Deque<Direction> result) {
        StringBuilder buf = new StringBuilder(result.size());

        Iterator<Direction> reverseIt = result.descendingIterator();

        while (reverseIt.hasNext()) {
            buf.append(reverseIt.next().inverse().simbol);
        }

        return buf.toString();
    }

    private double distance(XYPair p1, XYPair p2) {

        double dx = (double)p1.x - p2.x;
        double dy = (double)p1.y - p2.y;

        return Math.sqrt(dx * dx + dy * dy);
    }

    private boolean isLyingOnLine(XYPair point, XYPair lineStart, XYPair lineEnd) {
        double dist1 = distance(lineStart, point) + distance(point, lineEnd);
        double dist2 = distance(lineStart, lineEnd);
        return DoubleUtils.isEquals(dist1, dist2);
    }

    private void tryMoveInDirection(XYPair curPoint, List<XYPair> blocks, Direction direction, Deque<Direction> result,
                                    int pathLength) {

        Direction prevDirection = result.peekLast();

        // don't allow to move in same direction twice or opposite one (by turning 180 degree),
        // allow only 90 degree turn
        if (!result.isEmpty() && (direction == prevDirection || direction.inverse() == prevDirection)) {
            return;
        }

        XYPair nextPoint = direction.nextPoint(curPoint, pathLength);

        for (XYPair block : blocks) {

            // check if any 'block' lying on a line, curPoint<->nextPoint

            if (isLyingOnLine(block, curPoint, nextPoint)) {
                return;
            }
        }

        result.addLast(direction);
        findPathRec(nextPoint, blocks, result, pathLength - 1);
        result.pollLast();
    }


    private enum Direction {
        NORTH("n") {
            @Override
            XYPair nextPoint(XYPair basePoint, int length) {
                return new XYPair(basePoint.x, basePoint.y + length);
            }

            @Override
            Direction inverse() {
                return SOUTH;
            }
        },
        SOUTH("s") {
            @Override
            XYPair nextPoint(XYPair basePoint, int length) {
                return new XYPair(basePoint.x, basePoint.y - length);
            }

            @Override
            Direction inverse() {
                return NORTH;
            }
        },
        WEST("w") {
            @Override
            XYPair nextPoint(XYPair basePoint, int length) {
                return new XYPair(basePoint.x - length, basePoint.y);
            }

            @Override
            Direction inverse() {
                return EAST;
            }
        },
        EAST("e") {
            @Override
            XYPair nextPoint(XYPair basePoint, int length) {
                return new XYPair(basePoint.x + length, basePoint.y);
            }

            @Override
            Direction inverse() {
                return WEST;
            }
        };

        private final String simbol;

        Direction(String simbol) {
            this.simbol = simbol;
        }

        abstract XYPair nextPoint(XYPair basePoint, int length);

        abstract Direction inverse();
    }

    private static final class XYPair {

        static final XYPair ZERO = new XYPair(0, 0);

        final int x;
        final int y;

        public XYPair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            if (!(obj instanceof XYPair)) {
                return false;
            }

            XYPair other = (XYPair) obj;

            if (x != other.x) {
                return false;
            }

            return y == other.y;
        }

        @Override
        public int hashCode() {
            int hash = 17;

            hash = 31 * hash + x;
            hash = 31 * hash + y;
            return hash;
        }


        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

}
