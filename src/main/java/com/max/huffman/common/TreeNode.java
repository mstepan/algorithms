package com.max.huffman.common;

import java.util.Comparator;


public final class TreeNode {

    public static final Comparator<TreeNode> FREQ_ASC_CMP = Comparator.comparingInt(first -> first.value);

    public TreeNode left;
    public TreeNode right;
    public int size;

    public char ch;
    public int value;

    public static TreeNode createLeaf(char ch, int freq) {
        TreeNode node = new TreeNode();
        node.ch = ch;
        node.value = freq;
        node.size = 1;
        return node;
    }

    public static TreeNode createNode(TreeNode left, TreeNode right) {
        TreeNode node = new TreeNode();
        node.left = left;
        node.right = right;
        node.value = left.value + right.value;
        node.size = left.size + right.size + 1;
        return node;
    }

    public int size(){
        return size;
    }

    public boolean isLeaf() {
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
