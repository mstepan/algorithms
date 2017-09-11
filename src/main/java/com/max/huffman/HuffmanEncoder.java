package com.max.huffman;

import org.apache.log4j.Logger;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;


final class HuffmanEncoder {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private static final class TreeNode {

        private static final Comparator<TreeNode> FREQ_ASC_CMP = Comparator.comparingInt(first -> first.value);

        private static final TreeNode ZERO_NODE = TreeNode.createLeaf(Character.MIN_VALUE, 0);

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


    static void encode(Path inPath, Path outPath) {
        Map<Character, Integer> freq = calculateCharsFrequency(inPath);

        TreeNode root = buildEncodingTree(freq);

        writeEncodingTreeToFile(root, outPath);

//        printCharsEncodings(root, "", freq);

        //TODO:
    }

    private static Map<Character, Integer> calculateCharsFrequency(Path inPath) {
        Map<Character, Integer> freq = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(inPath)) {

            String line;
            while ((line = reader.readLine()) != null) {

                char[] arr = line.toCharArray();

                for (char ch : arr) {
                    freq.compute(ch, (curCh, count) -> count == null ? 1 : count + 1);
                }
            }
        }
        catch (IOException ioEx) {
            throw new IllegalStateException("Can't properly read file", ioEx);
        }

        return freq;
    }

    private static TreeNode buildEncodingTree(Map<Character, Integer> freq) {
        PriorityQueue<TreeNode> minHeap = new PriorityQueue<>(TreeNode.FREQ_ASC_CMP);

        minHeap.add(TreeNode.ZERO_NODE);

        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            minHeap.add(TreeNode.createLeaf(entry.getKey(), entry.getValue()));
        }

        while (minHeap.size() != 1) {
            TreeNode first = minHeap.poll();
            TreeNode second = minHeap.poll();

            TreeNode combinedNode = TreeNode.createNode(first, second);

            minHeap.add(combinedNode);
        }

        return minHeap.poll();
    }

    private static void writeEncodingTreeToFile(TreeNode root, Path outPath) {
        try {
            try (OutputStream fileOutStream = Files.newOutputStream(outPath);
                 BufferedOutputStream bufferedOutStream = new BufferedOutputStream(fileOutStream);
                 DataOutputStream dataOut = new DataOutputStream(bufferedOutStream)) {

                dataOut.writeInt(root.size);

                writeNodeRec(root, dataOut);
            }
        }
        catch (IOException ioEx) {
            LOG.error("Can't write encoding tree", ioEx);
        }
    }

    private static void writeNodeRec(TreeNode node, DataOutputStream dataOut) throws IOException {

        assert node != null : "null 'node' detected";

        dataOut.writeChar(node.ch);

        if (!node.isLeaf()) {
            writeNodeRec(node.left, dataOut);
            writeNodeRec(node.right, dataOut);
        }
    }

    private static void printCharsEncodings(TreeNode node, String code, Map<Character, Integer> freq) {

        assert node != null;

        if (node.isLeaf()) {
            char ch = node.ch;
            LOG.info("'" + ch + "': " + code + ", freq = " + freq.get(ch));
            return;
        }


        printCharsEncodings(node.left, code + "0", freq);
        printCharsEncodings(node.right, code + "1", freq);
    }

}
