package com.max.huffman.encoder;

import com.max.huffman.FileCharsIterator;
import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;


public final class HuffmanEncoder {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private HuffmanEncoder() {
    }


    public static void encode(Path inPath, Path outPath) {
        Map<Character, Integer> freq = calculateCharsFrequency(inPath);

        TreeNode root = buildEncodingTree(freq);

        writeEncodingTreeToFile(root, outPath);

        Map<Character, PrefixCode> encodingMap = getEncodingMap(root);

        try (FileCharsIterator it = new FileCharsIterator(inPath)) {
            while (it.hasNext()) {
                char ch = it.next();
                PrefixCode code = encodingMap.get(ch);
                LOG.info("'" + ch + "': " + code);
            }
        }
    }


    private static Map<Character, PrefixCode> getEncodingMap(TreeNode root) {
        Map<Character, PrefixCode> encodingMap = new HashMap<>();

        buildEncodingMapRec(root, encodingMap, 0, 0);

        return encodingMap;
    }

    private static void buildEncodingMapRec(TreeNode node, Map<Character, PrefixCode> encodingMap,
                                            int code, int bitsCount) {

        assert bitsCount <= Integer.SIZE : "'bitsCount' exceeded 32 bits";

        if (node.isLeaf()) {
            encodingMap.put(node.ch, new PrefixCode(code, bitsCount));
            return;
        }

        buildEncodingMapRec(node.left, encodingMap, code << 1, bitsCount + 1);
        buildEncodingMapRec(node.right, encodingMap, (code << 1) | 1, bitsCount + 1);
    }

    private static Map<Character, Integer> calculateCharsFrequency(Path inPath) {
        Map<Character, Integer> freq = new HashMap<>();

        try (FileCharsIterator it = new FileCharsIterator(inPath)) {
            while (it.hasNext()) {
                char ch = it.next();
                freq.compute(ch, (curCh, count) -> count == null ? 1 : count + 1);
            }
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

}
