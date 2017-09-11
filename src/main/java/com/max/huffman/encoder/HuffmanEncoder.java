package com.max.huffman.encoder;

import com.max.algs.io.BitOutputStream;
import com.max.huffman.common.FileCharsIterator;
import com.max.huffman.common.TreeNode;
import com.max.huffman.common.TreeNodeFileUtil;
import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Huffman file encoder utility class.
 */
public final class HuffmanEncoder {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private HuffmanEncoder() {
    }


    public static void encode(Path inPath, Path outPath) {
        Map<Character, Integer> freq = calculateCharsFrequency(inPath);

        // -1 to remove 'END character marker' from counting
        int uniqueCharsCount = freq.size() - 1;
        LOG.info("Unique chars count (compression): " + uniqueCharsCount);

        TreeNode root = buildEncodingTree(freq);

        TreeNodeFileUtil.writeEncodingTreeToFile(root, outPath);

        Map<Character, PrefixCode> encodingMap = getEncodingMap(root);

        writeMainContent(inPath, encodingMap, outPath);
    }

    private static Map<Character, Integer> calculateCharsFrequency(Path inPath) {
        Map<Character, Integer> freq = new HashMap<>();

        // add END character marker
        freq.put(Character.MIN_VALUE, 0);

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

    private static void writeMainContent(Path inPath, Map<Character, PrefixCode> encodingMap, Path outPath) {
        try (FileCharsIterator it = new FileCharsIterator(inPath)) {

            try (OutputStream outStream = Files.newOutputStream(outPath, StandardOpenOption.APPEND);
                 BufferedOutputStream bufOutStream = new BufferedOutputStream(outStream);
                 BitOutputStream bitOutStream = new BitOutputStream(bufOutStream)) {

                while (it.hasNext()) {

                    PrefixCode code = encodingMap.get(it.next());

                    bitOutStream.write(code.getValue(), code.getBitsCount());
                }

                PrefixCode endCode = encodingMap.get(Character.MIN_VALUE);
                bitOutStream.write(endCode.getValue(), endCode.getBitsCount());
            }
            catch (IOException ioEx) {
                throw new IllegalStateException("Can't properly open '" + outPath + "'", ioEx);
            }
        }
    }

}
