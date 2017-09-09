package com.max.huffman;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
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

        TreeNode left;
        TreeNode right;

        char ch;
        int value;

        static TreeNode createLeaf(char ch, int freq) {
            TreeNode node = new TreeNode();
            node.ch = ch;
            node.value = freq;
            return node;
        }

        static TreeNode createNode(TreeNode left, TreeNode right) {
            TreeNode node = new TreeNode();
            node.left = left;
            node.right = right;
            node.value = left.value + right.value;
            return node;
        }

        boolean isLeaf() {
            return left == null && right == null;
        }

        @Override
        public String toString() {
            return String.valueOf(ch) + ": " + value;
        }
    }


    static void encode(Path inPath, Path outPath) {
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

        PriorityQueue<TreeNode> minHeap = new PriorityQueue<>(TreeNode.FREQ_ASC_CMP);

        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            minHeap.add(TreeNode.createLeaf(entry.getKey(), entry.getValue()));
        }

        while (minHeap.size() != 1) {
            TreeNode first = minHeap.poll();
            TreeNode second = minHeap.poll();

            TreeNode combinedNode = TreeNode.createNode(first, second);

            minHeap.add(combinedNode);
        }

        TreeNode root = minHeap.poll();
        printCharsEncodings(root, "", freq);
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
