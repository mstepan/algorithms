package com.max.algs.ds.tree;

import com.google.common.collect.ImmutableList;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.AbstractMap.SimpleImmutableEntry;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Not thread safe complete binary tree which doesn't allow to store null values.
 */
public class BinaryTree<T> extends AbstractCollection<T> implements
        Collection<T>, Cloneable, java.io.Serializable {

    private static final long serialVersionUID = 4665019545884223055L;

    private static final Logger LOG = Logger.getLogger(BinaryTree.class);
    // Won't contain more then ceil( size/2 ) elements.
    private transient final Queue<Node<T>> emptyNodes = new ArrayDeque<>();
    private transient Node<T> root;
    private int size;

    /**
     * Do post-order traversation (left-right-parent) and find binary tree diameter.
     * time: O(N)
     * space: O(h) => O(N)
     */
    private static int findDiameterRec(Node cur, MutableInteger diameter) {

        if (cur == null) {
            return 0;
        }

        int leftDepth = findDiameterRec(cur.left, diameter);
        int rightDepth = findDiameterRec(cur.right, diameter);

        int curDiameter = leftDepth + rightDepth;

        if (curDiameter > diameter.value) {
            diameter.value = curDiameter;
        }

        return Math.max(leftDepth, rightDepth) + 1;
    }

    /**
     * Convert a given tree to its Sum Tree.
     * Use iterative approach instead of recursive.
     * <p>
     * time: O(N)
     * space: O(N)
     */
    public static void toSumTree(BinaryTree<Integer> tree) {

        if (tree.root == null) {
            return;
        }

        Map<Node<Integer>, Integer> handled = new HashMap<>();

        Deque<Node<Integer>> stack = new ArrayDeque<>();
        stack.push(tree.root);

        while (!stack.isEmpty()) {
            Node<Integer> cur = stack.pop();

            if (cur.isLeaf()) {
                handled.put(cur, cur.value);
                cur.value = 0;
            }
            else {
                if ((cur.left != null && handled.containsKey(cur.left)) ||
                        (cur.right != null && handled.containsKey(cur.right))) {

                    handled.put(cur, cur.value);

                    if (cur.left != null) {
                        cur.value = cur.left.value + handled.get(cur.left);
                    }

                    if (cur.right != null) {
                        cur.value += cur.right.value + handled.get(cur.right);
                    }

                }
                else {
                    stack.push(cur);
                    if (cur.right != null) {
                        stack.push(cur.right);
                    }

                    if (cur.left != null) {
                        stack.push(cur.left);
                    }
                }
            }

        }

    }

    private SimpleImmutableEntry<Integer, Integer> findMinMaxDistanceFromRoot() {

        assert root != null : "root is NULL";

        int minDistance = 0;
        int maxDistance = 0;

        Deque<SimpleImmutableEntry<Node<T>, Integer>> queue = new ArrayDeque<>();
        queue.add(new SimpleImmutableEntry<>(root, 0));

        List<SimpleImmutableEntry<Node<T>, Integer>> res = new ArrayList<>();

        while (!queue.isEmpty()) {

            SimpleImmutableEntry<Node<T>, Integer> cur = queue.poll();

            if (cur.getValue() < minDistance) {
                minDistance = cur.getValue();
            }

            if (cur.getValue() > maxDistance) {
                maxDistance = cur.getValue();
            }

            if (cur.getKey().left != null) {
                queue.add(new SimpleImmutableEntry<>(cur.getKey().left, cur
                        .getValue() - 1));
            }

            if (cur.getKey().right != null) {
                queue.add(new SimpleImmutableEntry<>(cur.getKey().right, cur
                        .getValue() + 1));
            }

            res.add(cur);
        }

        return new SimpleImmutableEntry<>(minDistance, maxDistance);
    }

    /**
     * time: O(N)
     * space: O(N)
     */
    public List<String> addSiblings() {

        if (root == null) {
            return ImmutableList.of();
        }

        List<Node<T>> roots = new ArrayList<>();

        int curLevelCount = 1;
        Deque<Node<T>> level = new ArrayDeque<>();
        level.add(root);

        Node<T> prev = null;
        int nextLevelCount = 0;

        while (!level.isEmpty()) {

            // beginning of a new level
            if (curLevelCount == 0) {
                curLevelCount = nextLevelCount;
                nextLevelCount = 0;
                prev = null;
            }

            Node<T> cur = level.poll();

            if (prev != null) {
                prev.next = cur;
            }
            else {
                roots.add(cur);
            }

            if (cur.left != null) {
                level.add(cur.left);
                ++nextLevelCount;
            }

            if (cur.right != null) {
                level.add(cur.right);
                ++nextLevelCount;
            }
            prev = cur;
            --curLevelCount;
        }

        ImmutableList.Builder<String> res = ImmutableList.builder();


        for (Node<T> cur : roots) {

            StringBuilder buf = new StringBuilder();
            buf.append("[");

            Node<T> curLevel = cur;

            while (curLevel != null) {
                buf.append(curLevel.value);
                curLevel = curLevel.next;
                if (curLevel != null) {
                    buf.append(", ");
                }
            }
            buf.append("]");

            res.add(buf.toString());
        }

        return res.build();
    }

    public int diameter() {

        if (isEmpty()) {
            return 0;
        }

        MutableInteger diameterValue = new MutableInteger(0);

        findDiameterRec(root, diameterValue);

        return diameterValue.value;
    }

    /**
     * time: O(N)
     * space: O(h) => O(N)
     */
    public String toPreorderString() {
        StringBuilder buf = new StringBuilder();

        Deque<Node> stack = new ArrayDeque<>();

        Node cur = root;

        while (cur != null) {
            buf.append(cur.value).append(", ");

            if (cur.isLeaf()) {
                if (stack.isEmpty()) {
                    break;
                }

                cur = stack.pop();
            }
            else {

                if (cur.right != null) {
                    stack.push(cur.right);
                }

                if (cur.left != null) {
                    cur = cur.left;
                }
            }
        }

        return buf.toString();
    }

    /**
     * 1) Write the code to delete nodes in a binary tree which don’t lie in any path from root to leaf with sum>=k.
     */
    public void cleanTree(int sum) {

        Deque<Node> nodesToDelete = new ArrayDeque<>();
        cleanTree((Node) root, 0, sum, nodesToDelete);

        if (!nodesToDelete.isEmpty()) {
            root = null;
        }
    }

    private void cleanTree(Node<Integer> cur, int pathSum, int sum, Deque<Node> nodeToDelete) {

        if (cur == null) {
            return;
        }

        if (cur.isLeaf()) {
            if (cur.value + pathSum < sum) {
                nodeToDelete.addLast(cur);
            }

            return;
        }

        int curSum = cur.value + pathSum;

        if (curSum >= sum) {
            return;
        }

        cleanTree(cur.left, curSum, sum, nodeToDelete);
        cleanTree(cur.right, curSum, sum, nodeToDelete);

        while (!nodeToDelete.isEmpty() &&
                (nodeToDelete.peekLast() == cur.left || nodeToDelete.peekLast() == cur.right)) {

            Node<T> node = nodeToDelete.pollLast();

            if (cur.left == node) {
                cur.left = null;
            }
            else {
                cur.right = null;
            }
        }

        if (cur.isLeaf() && curSum < sum) {
            nodeToDelete.addLast(cur);
        }
    }

    /**
     * time: O(N) space: O(h)
     */
    public String sortedOrderForMinFirst() {

        if (root == null) {
            return "";
        }

        StringBuilder buf = new StringBuilder(size << 2);

        Deque<Node<T>> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            Node<T> cur = stack.pop();

            if (cur != root) {
                buf.append(",");
            }
            buf.append(cur.value);

            if (cur.right != null) {
                stack.push(cur.right);
            }

            if (cur.left != null) {
                stack.push(cur.left);
            }
        }

        return buf.toString();
    }

    /**
     * time: O(N)
     * space: O(N)
     *
     * @return true - if value exists in binary tree,
     * false - otherwise.
     */
    @Override
    public boolean contains(Object value) {

        checkArgument(value != null, "Can't execute contains using null value");

        if (root == null) {
            return false;
        }

        Queue<Node<T>> queue = new ArrayDeque<>();
        queue.add(root);

        Node cur;

        while (!queue.isEmpty()) {

            cur = queue.poll();

            if (cur.value.equals(value)) {
                return true;
            }

            if (cur.left != null) {
                queue.add(cur.left);
            }

            if (cur.right != null) {
                queue.add(cur.right);
            }
        }

        return false;
    }

    /**
     * Check if value exists in min-first BS tree.
     */
    @SuppressWarnings("unchecked")
    public boolean containsInMinFirst(T key) {

        Node<T> cur = root;

        while (cur != null) {

            if (((Comparable<T>) cur.value).compareTo(key) > 0) {
                return false;
            }

            if (cur.value.equals(key)) {
                return true;
            }

            if (cur.right == null
                    || ((Comparable<T>) cur.right.value).compareTo(key) > 0) {
                cur = cur.left;
            }
            else {
                cur = cur.right;
            }
        }

        return false;
    }

    /**
     * Return vertical slices of current binary tree.
     * <p>
     * time: O(N) spaceL O(N)
     */
    public List<List<T>> verticalSlices() {

        if (root == null) {
            return new ArrayList<>();
        }

        SimpleImmutableEntry<Integer, Integer> distance = findMinMaxDistanceFromRoot();

        int min = Math.abs(distance.getKey());
        int max = distance.getValue();

        int bucketsCount = min + max + 1;
        List<List<T>> buckets = new ArrayList<>(bucketsCount);

        for (int i = 0; i < bucketsCount; i++) {
            buckets.add(new ArrayList<T>());
        }

        Deque<SimpleImmutableEntry<Node<T>, Integer>> queue = new ArrayDeque<>();
        queue.add(new SimpleImmutableEntry<>(root, 0));

        while (!queue.isEmpty()) {
            SimpleImmutableEntry<Node<T>, Integer> cur = queue.poll();

            int curDistance = cur.getValue();

            buckets.get(curDistance + min).add(cur.getKey().value);

            if (cur.getKey().left != null) {
                queue.add(new SimpleImmutableEntry<>(cur.getKey().left, cur
                        .getValue() - 1));
            }

            if (cur.getKey().right != null) {
                queue.add(new SimpleImmutableEntry<>(cur.getKey().right, cur
                        .getValue() + 1));
            }
        }

        return buckets;
    }

    /**
     * Do pre order traversation and check if binary tree satisfy binary search tree properties.
     * time: O(N)
     * space: O(h)
     */
    public boolean isBinarySearchTree() {

        if (root == null) {
            return true;
        }

        Queue<Node<T>> queue = new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node<T> cur = queue.poll();

            if (cur.left != null) {

                if (Comparable.class.cast(cur.left.value).compareTo(cur.value) > 0) {
                    return false;
                }

                queue.add(cur.left);
            }

            if (cur.right != null) {
                if (Comparable.class.cast(cur.right.value).compareTo(cur.value) < 0) {
                    return false;
                }

                queue.add(cur.right);
            }
        }

        return true;
    }

    public boolean isBalanced() {

        if (root == null) {
            return true;
        }

        int leftHeight = heightRec(root.left);
        int rightHeight = heightRec(root.right);

        int difference = Math.abs(leftHeight - rightHeight);

        return difference < 2;

    }

    /**
     * time: O(N) space: O(h)
     */
    public boolean isSymmetric() {
        if (root == null) {
            return true;
        }
        return isSymRec(root.left, root.right);
    }

	/*

     */

    private boolean isSymRec(Node<T> left, Node<T> right) {

        // one is NULL another is not
        if ((left == null) ^ (right == null)) {
            return false;
        }

        if (left == null && right == null) {
            return true;
        }

        return isSymRec(left == null ? null : left.right, right == null ? null
                : right.left)
                && isSymRec(left == null ? null : left.left,
                right == null ? null : right.right);
    }

    public int height() {
        if (size == 0) {
            return 0;
        }
        return heightRec(root) - 1;
    }

    /**
     * time: O(N) space: O(h)
     * <p>
     * Not tail recursive.
     */
    private int heightRec(Node<T> cur) {
        if (cur == null) {
            return 0;
        }

        return 1 + Math.max(heightRec(cur.left), heightRec(cur.right));
    }

    /**
     * Make vertical slice for each level of tree.
     * <p>
     * time: O(N) space: O(N)
     */
    public List<List<T>> verticalTraversation() {

        if (root == null) {
            return Collections.emptyList();
        }

        int left = 0;

        Node<T> curLeft = root.left;

        while (curLeft != null) {
            curLeft = curLeft.left;
            ++left;
        }

        int right = 0;

        Node<T> curRight = root.right;

        while (curRight != null) {
            curRight = curRight.right;
            ++right;
        }

        List<List<T>> verticalLevels = new ArrayList<>();

        for (int i = 0; i < left + right + 1; i++) {
            verticalLevels.add(new ArrayList<T>());
        }

        final int offset = left;

        Deque<VerticalSlice<T>> stack = new ArrayDeque<>();
        stack.add(new VerticalSlice<>(root, 0));

        while (!stack.isEmpty()) {

            VerticalSlice<T> slice = stack.poll();

            verticalLevels.get(slice.level + offset).add(slice.entry.value);

            if (slice.entry.left != null) {
                stack.add(new VerticalSlice<>(slice.entry.left, slice.level - 1));
            }

            if (slice.entry.right != null) {
                stack.add(new VerticalSlice<>(slice.entry.right,
                        slice.level + 1));
            }

        }

        return verticalLevels;
    }

    /**
     * Find least common ancestor of two nodes.
     * <p>
     * time: O(N)
     * space: O(h), can be up to O(N) for unbalanced tree.
     */
    public Optional<T> lca(T first, T second) {

        Node[] res = new Node[1];

        lcaRec(root, first, second, res);

        return res[0] == null ? Optional.empty() : Optional.of((T) res[0].value);
    }

    private int lcaRec(Node<T> cur, T first, T second, Node[] res) {

        if (cur == null || res[0] != null) {
            return 0;
        }

        boolean found = false;

        if (first.equals(cur.value) || second.equals(cur.value)) {
            found = true;
        }

        int left = lcaRec(cur.left, first, second, res);
        int right = lcaRec(cur.right, first, second, res);

        if (left == 1 && right == 1 && res[0] == null) {
            res[0] = cur;
        }

        if (found && (left == 1 || right == 1) && res[0] == null) {
            res[0] = cur;
        }

        return left + right + (found ? 1 : 0);
    }

    public String levelSymTrav() {
        StringBuilder buf = new StringBuilder();

        Deque<Node<T>> levelDeque = new ArrayDeque<>();
        levelDeque.add(root);

        while (!levelDeque.isEmpty()) {

            Deque<Node<T>> clonedDeque = new ArrayDeque<>(levelDeque);

            while (!clonedDeque.isEmpty()) {
                buf.append(clonedDeque.poll()).append(", ");

                if (!clonedDeque.isEmpty()) {
                    buf.append(clonedDeque.pollLast()).append(", ");
                }
            }

            Node<T> lastNode = levelDeque.peekLast();

            while (true) {

                Node<T> node = levelDeque.poll();

                if (node.left != null) {
                    levelDeque.add(node.left);
                }

                if (node.right != null) {
                    levelDeque.add(node.right);
                }

                if (node == lastNode) {
                    break;
                }
            }
        }

        return buf.toString();
    }

    /**
     * time: O(N) space: O(N/2)
     */
    public void levelTraversation(ITreeVisitor<T> visitor) {

        if (root == null) {
            return;
        }

        Queue<Node<T>> curLevel = new ArrayDeque<>();
        curLevel.add(root);
        int count = 1;
        int levelIndex = 0;

        while (!curLevel.isEmpty()) {

            int newCount = 0;

            while (count > 0) {

                Node<T> node = curLevel.poll();

                visitor.visit(node.value, levelIndex);

                if (node.left != null) {
                    curLevel.add(node.left);
                    ++newCount;
                }

                if (node.right != null) {
                    curLevel.add(node.right);
                    ++newCount;
                }

                --count;
            }

            count = newCount;
            ++levelIndex;
        }
    }

    /**
     * Given a binary tree, print all paths that sum to a certain value.
     *
     * @param expectedValue
     */
    public void printAllPathsSummedToValue(int expectedValue) {

        if (isEmpty()) {
            return;
        }

        Deque<T> path = new ArrayDeque<>();

        checkPath(root, 0, expectedValue, path);

    }

    private void checkPath(Node<T> cur, int actualSum, int expectedSum,
                           Deque<T> path) {

        if (cur == null || actualSum > expectedSum) {
            return;
        }

        actualSum += (Integer) cur.value;
        path.push(cur.value);

        if (actualSum == expectedSum && cur.isLeaf()) {
            LOG.info(path);
            path.pop();
        }
        else {
            checkPath(cur.left, actualSum, expectedSum, path);
            checkPath(cur.right, actualSum, expectedSum, path);

            path.pop();
        }

    }

    /**
     * Do pre-order traversation and check if a binary tree is a binary search
     * tree.
     * <p>
     * time: O(N) space: O(lgN)
     */
    public boolean isBstree(Comparator<T> cmp) {

        Deque<Node<T>> stack = new ArrayDeque<>();

        Node<T> cur = root;

        while (cur != null) {

            if (cur.left != null && cmp.compare(cur.left.value, cur.value) > 0) {
                return false;
            }

            if (cur.right != null
                    && cmp.compare(cur.right.value, cur.value) < 0) {
                return false;
            }

            if (cur.left != null) {
                if (cur.right != null) {
                    stack.push(cur.right);
                }

                cur = cur.left;
            }
            else if (cur.right != null) {
                cur = cur.right;
            }
            else {
                cur = stack.pollFirst();
            }
        }

        return true;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {

        if (root == null) {
            return "EMPTY";
        }

        StringBuilder buf = new StringBuilder();

        Deque<Node<T>> stack = new ArrayDeque<>();

        Node<T> cur = root;

        while (cur != null) {

            if (cur.left != null) {
                stack.push(cur);
                cur = cur.left;
            }
            else {

                buf.append(cur.value).append(",");

                if (cur.right != null) {
                    cur = cur.right;
                }
                else {
                    Node<T> nextNode = null;

                    while (!stack.isEmpty()) {

                        cur = stack.pop();

                        buf.append(cur.value).append(",");

                        if (cur.right != null) {
                            nextNode = cur.right;
                            break;
                        }
                    }

                    cur = nextNode;
                }
            }

        }

        return buf.toString();
    }

    public List<T> extractLeafs() {

        List<T> leafs = new ArrayList<>();

        if (size == 1) {
            leafs.add(root.value);
            root = null;
        }
        else {
            addIfLeaf(root, null, leafs);
        }

        return leafs;
    }

    public void addIfLeaf(Node<T> cur, Node<T> parent, List<T> leafs) {

        if (cur == null) {
            return;
        }

        if (cur.isLeaf()) {

            leafs.add(cur.value);

            if (parent.left == cur) {
                parent.left = null;
            }
            else {
                parent.right = null;
            }
        }
        else {
            addIfLeaf(cur.left, cur, leafs);
            addIfLeaf(cur.right, cur, leafs);
        }
    }

    public boolean add(T value, T parentValue, NodeDirection direction) {

        Node<T> parent = findNode(root, parentValue);

        if (parent == null) {
            return false;
        }

        if (direction == NodeDirection.LEFT) {
            if (parent.left != null) {
                return false;
            }

            parent.left = new Node<T>(value);
        }
        else {
            if (parent.right != null) {
                return false;
            }

            parent.right = new Node<T>(value);
        }

        ++size;
        return true;

    }

    @Override
    public boolean add(T value) {

        if (value == null) {
            throw new IllegalArgumentException("Can't store 'NULL' value in "
                    + BinaryTree.class.getCanonicalName());
        }

        Node<T> newNode = new Node<>(value);

        if (size == 0) {
            root = newNode;
            emptyNodes.add(root);
        }
        else {
            Node<T> lastNode = emptyNodes.peek();

            if (lastNode.left != null && lastNode.right != null) {
                emptyNodes.poll();
                lastNode = emptyNodes.peek();
            }

            if (lastNode.left == null) {
                lastNode.left = newNode;
            }
            else {
                lastNode.right = newNode;
            }

            emptyNodes.add(newNode);

        }

        ++size;
        return true;
    }

    /**
     * Get all nodes that are 'distance' hopes way from leaf.
     * <p>
     * time: O(N) space: O(lgN)
     */
    public List<T> getNodesWithDistanceFromLeaf(int distance) {
        List<T> values = new ArrayList<>();
        checkRec(root, distance, values);
        return values;
    }

    private Set<Integer> checkRec(Node<T> cur, int distance, List<T> values) {

        assert cur != null : "'cur' is NULL";

        if (cur.isLeaf()) {
            return Collections.singleton(1);
        }

        Set<Integer> leftSide = cur.left == null ? Collections.emptySet()
                : checkRec(cur.left, distance, values);
        Set<Integer> rightSide = cur.right == null ? Collections.emptySet()
                : checkRec(cur.right, distance, values);

        Set<Integer> combined = new HashSet<>(leftSide);
        combined.addAll(rightSide);

        Set<Integer> res = new HashSet<>();

        for (int depth : combined) {
            if (depth + 1 < distance) {
                res.add(depth + 1);
            }
            else if (depth + 1 == distance) {
                values.add(cur.value);

            }
        }

        return res;
    }

    protected Node<T> root() {
        return root;
    }

    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Write a program to return the highest value from a binary tree (note: not
     * BST).
     */
    public T findMax(Comparator<T> comparator) {

        if (size == 0) {
            return null;
        }

        return findMax(root, comparator);
    }

    /**
     * time: O(N)
     * space: O(N)
     */
    public List<Integer> getVisibleFromTop() {

        if (isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> visibleValues = new ArrayList<>();

        Queue<NodeAndVerLevel<Integer>> q = new ArrayDeque<>();
        q.add(new NodeAndVerLevel<>((Node<Integer>) root, 0));

        int left = 1;
        int right = 0;

        while (!q.isEmpty()) {
            NodeAndVerLevel<Integer> wrapper = q.poll();

            if (wrapper.level < left) {
                visibleValues.add(wrapper.node.value);
                left = wrapper.level;
            }
            else if (wrapper.level > right) {
                visibleValues.add(wrapper.node.value);
                right = wrapper.level;
            }

            if (wrapper.node.left != null) {
                q.add(new NodeAndVerLevel<>((Node<Integer>) wrapper.node.left, wrapper.level - 1));
            }

            if (wrapper.node.right != null) {
                q.add(new NodeAndVerLevel<>((Node<Integer>) wrapper.node.right, wrapper.level + 1));
            }


        }

        return visibleValues;
    }

    /**
     * time: O(N) space: O(lgN)
     */
    private T findMax(Node<T> cur, Comparator<T> comparator) {

        T maxValue = cur.value;

        if (cur.left != null) {

            T leftMax = findMax(cur.left, comparator);

            if (comparator.compare(leftMax, maxValue) > 0) {
                maxValue = leftMax;
            }
        }

        if (cur.right != null) {
            T rightMax = findMax(cur.right, comparator);

            if (comparator.compare(rightMax, maxValue) > 0) {
                maxValue = rightMax;
            }
        }

        return maxValue;
    }

    private Node<T> findNode(Node<T> node, T value) {

        if (node == null) {
            return null;
        }

        if (node.value.equals(value)) {
            return node;
        }

        Node<T> foundNode = findNode(node.left, value);

        if (foundNode != null) {
            return foundNode;
        }

        return findNode(node.right, value);
    }

    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        // don't forget to copy 'emptyNodes' queue
    }

    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        s.defaultWriteObject();
    }

    public static enum NodeDirection {
        LEFT, RIGHT;
    }

    private static final class MutableInteger {
        int value;

        MutableInteger(int value) {
            this.value = value;
        }
    }

    static class VerticalSlice<K> {

        final Node<K> entry;
        final int level;

        VerticalSlice(Node<K> entry, int level) {
            super();
            this.entry = entry;
            this.level = level;
        }

    }

    private static class NodeAndVerLevel<T> {

        final Node<T> node;
        final int level;

        public NodeAndVerLevel(Node<T> node, int level) {
            this.node = node;
            this.level = level;
        }


        @Override
        public String toString() {
            return node.value + ", level: " + level;
        }

    }

    static final class Node<U> {

        U value;
        Node<U> left;
        Node<U> right;
        Node<U> next;

        Node(U value) {
            this.value = value;
        }

        boolean isLeaf() {
            return left == null && right == null;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    private final class BinaryTreeIterator implements Iterator<T> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            return null;
        }

        @Override
        public void remove() {

        }

    }

}
