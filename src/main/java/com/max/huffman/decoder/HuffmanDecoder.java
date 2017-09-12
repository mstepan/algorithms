package com.max.huffman.decoder;

import com.max.algs.io.BitInputStream;
import com.max.huffman.common.TreeNode;
import com.max.huffman.common.TreeNodeFileUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Huffman file decoder utility class.
 */
public final class HuffmanDecoder {

    private HuffmanDecoder() {
    }

    public static void decode(Path encodedPath, Path decodedPath) {

        TreeNode root = TreeNodeFileUtil.readEncodingTreeFromFile(encodedPath);

        try (InputStream inStream = Files.newInputStream(encodedPath);
             BufferedInputStream bufInStream = new BufferedInputStream(inStream);
             BitInputStream bitInStream = new BitInputStream(bufInStream)) {

            try (OutputStream out = Files.newOutputStream(decodedPath);
                 DataOutputStream dataOut = new DataOutputStream(out)) {
                while (true) {

                    TreeNode cur = root;

                    while (!cur.isLeaf()) {

                        int bitValue = bitInStream.read();

                        // go left
                        if (bitValue == 0) {
                            cur = cur.left;
                        }
                        // go right
                        else {
                            cur = cur.right;
                        }
                    }

                    if (cur.ch == TreeNodeFileUtil.END_MARKER) {
                        break;
                    }

                    dataOut.writeChar(cur.ch);
                }
            }
        }
        catch (IOException ioEx) {
            throw new IllegalStateException(ioEx);
        }
    }

}
