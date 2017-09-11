package com.max.huffman.decoder;

import com.max.huffman.common.TreeNode;
import com.max.huffman.common.TreeNodeFileUtil;

import java.nio.file.Path;

/**
 * Huffman file decoder utility class.
 */
public final class HuffmanDecoder {

    private HuffmanDecoder() {
    }

    public static void decode(Path encodedPath, Path decodedPath) {

        TreeNode root = TreeNodeFileUtil.readEncodingTreeFromFile(encodedPath);

        //TODO:
    }

}
