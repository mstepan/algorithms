package com.max.huffman.encoder;

import java.util.Comparator;


final class TreeNode {

    public static final Comparator<TreeNode> FREQ_ASC_CMP = Comparator.comparingInt(first -> first.value);

    public static final TreeNode ZERO_NODE = TreeNode.createLeaf(Character.MIN_VALUE, 0);

    TreeNode left;
    TreeNode right;
    int size;

    char ch;
    int value;

    static TreeNode createLeaf(char ch, int freq) {
        TreeNode node = new TreeNode();
        node.ch = ch;
        node.value = freq;
        node.size = 1;
        return node;
    }

    static TreeNode createNode(TreeNode left, TreeNode right) {
        TreeNode node = new TreeNode();
        node.left = left;
        node.right = right;
        node.value = left.value + right.value;
        node.size = left.size + right.size + 1;
        return node;
    }

    boolean isLeaf() {
        return left == null && right == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

       TreeNode treeNode = (TreeNode) o;

        if (ch != treeNode.ch) return false;
        if (value != treeNode.value) return false;
        if (left != null ? !left.equals(treeNode.left) : treeNode.left != null) return false;
        return right != null ? right.equals(treeNode.right) : treeNode.right == null;
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        result = 31 * result + (int) ch;
        result = 31 * result + value;
        return result;
    }

    @Override
    public String toString() {
        return String.valueOf(ch) + ": " + value;
    }
}
