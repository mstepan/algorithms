package com.max.algs.codility;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * There is no food in your fridge and you are hungry. You want to go to a local store and buy some food. You have to hurry as some of the shops will close soon.
 * There are N squares in your neighborhood and M direct roads connecting them. The squares are numbered from 0 to N − 1. You are living in square 0 and can reach it in 0 seconds. The stores are located in the squares, one in each of them. You are given a map of the neighborhood in the form of four zero-indexed arrays A, B, C and D. Each of the arrays A, B, C contains M integers, while D contains N integers.
 * For each I (0 ≤ I < M), the walking distance between squares A[I] and B[I] is C[I] seconds (in either direction).
 * There can be multiple roads connecting the same pair of squares, or a road with both ends entering the same square.
 * It is possible that some roads go through tunnels or over bridges (that is, the graph of squares and roads doesn't have to be planar).
 * It is not guaranteed that you are able to reach all the squares.
 * For each J (0 ≤ J < N), the shop at square J will close in D[J] seconds (if D[J] = −1, then the store is already closed);
 * it is possible to buy the food even if you reach the shop at the very last second, when it closes.
 * Write a function:
 * class Solution { public int solution(int[] A,int[] B,int[] C,int[] D); }
 * that, given arrays A, B, C and D, returns the minimum time (in seconds) needed to reach an open store. If it is impossible, it should return −1.
 * For example, given:
 * A[0] = 0    B[0] = 1    C[0] = 2    D[0] = -1
 * A[1] = 1    B[1] = 2    C[1] = 3    D[1] = 1
 * A[2] = 3    B[2] = 2    C[2] = 4    D[2] = 3
 * A[3] = 1    B[3] = 3    C[3] = 5    D[3] = 8
 * A[4] = 2    B[4] = 0    C[4] = 7
 * A[5] = 2    B[5] = 1    C[5] = 5
 * <p>
 * the function should return 7. To reach the closest open shop you should follow the path: 0 −> 1 −> 3.
 * However, if given, for example:
 * A[0] = 0     D[0] = -1
 * B[0] = 1     D[1] = 6
 * C[0] = 10    D[2] = 8
 * the function should return −1, as you will not be able to reach square 1 in less than 10 seconds, and you cannot reach square 2 at all.
 * Assume that:
 * M is an integer within the range [0..10,000];
 * N is an integer within the range [0..100];
 * each element of array A is an integer within the range [0..99];
 * each element of array B is an integer within the range [0..99];
 * each element of array C is an integer within the range [0..100,000];
 * each element of array D is an integer within the range [−1..1,000,000,000].
 * Complexity:
 * expected worst-case time complexity is O(N2);
 * expected worst-case space complexity is O(N2), beyond input storage (not counting the storage required for input arguments).
 * Elements of input arrays can be modified.
 */
public class Hydrogenium {

    private static final Logger LOG = Logger.getLogger(Hydrogenium.class);

    private Hydrogenium() throws Exception {

        int[] a = {0, 1, 2, 1};
        int[] b = {1, 2, 3, 3};
        int[] c = {2, 3, 4, 5};
        int[] d = {-1, 1, -1, 8};

        int shortestPath = solution(a, b, c, d);

        LOG.info(shortestPath);

    }

    public static void main(String[] args) {
        try {
            new Hydrogenium();
        }
        catch (Exception ex) {
            LOG.error(ex);
        }
    }

    public int solution(int[] a, int[] b, int[] c, int[] d) {

        Queue<Vertex> queue = new PriorityQueue<Vertex>();

        Set<Integer> uniqueSet = new HashSet<Integer>();

        queue.add(new Vertex(0, 0));
        uniqueSet.add(0);

        for (int i = 0; i < a.length; i++) {
            if (!uniqueSet.contains(a[i])) {
                queue.add(new Vertex(a[i], Integer.MAX_VALUE));
                uniqueSet.add(a[i]);
            }
        }

        for (int i = 0; i < b.length; i++) {
            if (!uniqueSet.contains(b[i])) {
                queue.add(new Vertex(b[i], Integer.MAX_VALUE));
                uniqueSet.add(b[i]);
            }
        }

        Map<Integer, Integer> visited = new HashMap<Integer, Integer>();

        while (!queue.isEmpty()) {
            Vertex cur = queue.poll();
            visited.put(cur.vertex, cur.weight);

            for (int i = 0; i < a.length; i++) {
                if (a[i] == cur.vertex && !visited.containsKey(b[i])) {
                    checkQueue(queue, b[i], cur.weight + c[i]);
                }
            }

            for (int i = 0; i < b.length; i++) {
                if (b[i] == cur.vertex && !visited.containsKey(a[i])) {
                    checkQueue(queue, a[i], cur.weight + c[i]);
                }
            }
        }

        int min = Integer.MAX_VALUE;

        for (Map.Entry<Integer, Integer> entry : visited.entrySet()) {
            int vertex = entry.getKey();
            int weight = entry.getValue();

            if (vertex < d.length && d[vertex] > 0 && weight <= d[vertex]) {
                min = Math.min(min, weight);
            }
        }

        return min == Integer.MAX_VALUE ? -1 : min;
    }

    private void checkQueue(Queue<Vertex> queue, int node, int newWeight) {

        Iterator<Vertex> it = queue.iterator();
        Vertex newVer = null;

        while (it.hasNext()) {
            Vertex cur = it.next();
            if (cur.vertex == node) {
                if (cur.weight > newWeight) {
                    it.remove();
                    newVer = new Vertex(node, newWeight);
                    break;
                }
                else {
                    break;
                }
            }
        }

        if (newVer != null) {
            queue.add(newVer);
        }
    }

    static class Vertex implements Comparable<Vertex> {

        int vertex;
        int weight;

        Vertex(int vertex, int weight) {
            super();
            this.vertex = vertex;
            this.weight = weight;
        }

        @Override
        public int compareTo(Vertex other) {

            if (weight > other.weight) {
                return 1;
            }
            else if (weight < other.weight) {
                return -1;
            }
            return 0;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + vertex;
            result = prime * result + weight;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Vertex other = (Vertex) obj;
            if (vertex != other.vertex)
                return false;
            if (weight != other.weight)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "[" + vertex + ", weight = " + weight + "]";
        }

    }

}
