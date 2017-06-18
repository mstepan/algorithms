package com.max.algs.ds.tree;

import com.max.algs.ds.Pair;
import com.max.algs.ds.heap.BinaryHeap;
import org.apache.log4j.Logger;

import java.util.*;


/**
 * General linked tree structure.
 *
 * @author Maksym Stepanenko.
 */
public class WeightedTree<T> extends AbstractCollection<T> {

    private static final Logger LOG = Logger.getLogger(WeightedTree.class);

    private Node<T> root;

    private int size;

    private transient int modCount = 0;


    public WeightedTree() {
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Add element as root.
     */
    @Override
    public boolean add(T value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Add element as 'child' to parent.
     */
    public void add(T value, T parent, int weight) {

        if (root == null) {
            root = new Node<>(value, 0);
        }
        else {
            Node<T> newNode = new Node<>(value, weight);

            Node<T> parentNode = findNode(parent);

            if (parentNode == null) {
                throw new IllegalArgumentException("Can't find parent node '" + parent + "'");
            }
            parentNode.children.add(newNode);
        }
        ++size;
        ++modCount;
    }

    /**
     * time: O(N)
     * space: O(N)
     */
    private Node<T> findNode(T value) {

        if (root == null) {
            return null;
        }

        Deque<Node<T>> queue = new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node<T> cur = queue.poll();
            if (cur.value.equals(value)) {
                return cur;
            }

            queue.addAll(cur.children);
        }

        return null;
    }

    public void printByLevels() {

        if (isEmpty()) {
            LOG.info("EMPTY");
            return;
        }

        Deque<Node<T>> queue = new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty()) {

            Deque<Node<T>> nextLevelQueue = new ArrayDeque<>();
            StringBuilder levelStr = new StringBuilder();

            while (!queue.isEmpty()) {
                Node<T> cur = queue.poll();
                nextLevelQueue.addAll(cur.children);

                if (levelStr.length() > 0) {
                    levelStr.append(", ");
                }
                levelStr.append(cur.value);
            }

            LOG.info(levelStr);

            queue = nextLevelQueue;
        }
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public Iterator<T> iterator() {
        return new LevelIterator(modCount);
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Find diameter of a tree. Max path length.
     * time: O(N)
     * space: O(N)
     */
    public int findDiameter() {
        Map<Node<T>, Pair<Integer, Integer>> twoMaxWeights = new HashMap<>();
        postOrderTravRec(root, twoMaxWeights);
        return findDiameterRec(root, twoMaxWeights);
    }

    /**
     * time: O(N)
     * space: O(N)
     */
    private void postOrderTravRec(Node<T> cur, Map<Node<T>, Pair<Integer, Integer>> twoMaxWeights) {

        if (cur.isLeaf()) {
            twoMaxWeights.put(cur, new Pair<>(0, 0));
            return;
        }

        for (Node<T> child : cur.children) {
            postOrderTravRec(child, twoMaxWeights);
        }

        BinaryHeap<Integer> maxHeap = BinaryHeap.maxHeap(2);

        for (Node<T> child : cur.children) {
            maxHeap.add(child.weight + twoMaxWeights.get(child).getFirst());
        }

        if (maxHeap.size() == 1) {
            twoMaxWeights.put(cur, new Pair<>(maxHeap.poll(), 0));
        }
        else {
            twoMaxWeights.put(cur, new Pair<>(maxHeap.poll(), maxHeap.poll()));
        }
    }

    /**
     * Use divide and conquer technique.
     * time: O(N)
     * spaceL O(N)
     */
    private int findDiameterRec(Node<T> cur, Map<Node<T>, Pair<Integer, Integer>> twoMaxWeights) {

        if (cur.isLeaf()) {
            return 0;
        }

        int maxDiameter = 0;

        for (Node<T> child : cur.children) {
            maxDiameter = Math.max(maxDiameter, findDiameterRec(child, twoMaxWeights));
        }

        Pair<Integer, Integer> maxWeights = twoMaxWeights.get(cur);

        maxDiameter = Math.max(maxDiameter, maxWeights.getFirst() + maxWeights.getSecond());

        return maxDiameter;
    }

    private static final class Node<U> {

        final U value;
        final int weight;
        List<Node<U>> children = new ArrayList<>();

        Node(U value, int weight) {
            super();
            this.value = value;
            this.weight = weight;
        }

        boolean isLeaf() {
            return children.isEmpty();
        }

        @Override
        public String toString() {
            return String.valueOf(value) + ": " + weight;
        }
    }

    private final class LevelIterator implements Iterator<T> {

        final int modSnapshot;
        final Deque<Node<T>> items = new ArrayDeque<>();

        LevelIterator(int modSnapshot) {
            this.modSnapshot = modSnapshot;
            if (!isEmpty()) {
                items.add(WeightedTree.this.root);
            }
        }

        @Override
        public boolean hasNext() {
            return !items.isEmpty();
        }

        @Override
        public T next() {

            if (modSnapshot != modCount) {
                throw new ConcurrentModificationException();
            }

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node<T> node = items.poll();

            for (Node<T> child : node.children) {
                items.add(child);
            }

            return node.value;
        }

    }

}
