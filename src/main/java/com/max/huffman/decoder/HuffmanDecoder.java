package com.max.huffman.decoder;

import com.max.algs.io.BitInputStream2;
import com.max.huffman.common.TreeNode;
import com.max.huffman.common.TreeNodeFileUtil;
import org.apache.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Huffman file decoder utility class.
 */
public final class HuffmanDecoder {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private HuffmanDecoder() {
    }

    public static void decode(Path encodedPath, Path decodedPath) {

        try (RandomAccessFile encodedFileRandom = new RandomAccessFile(encodedPath.toFile().toString(), "r")) {

            TreeNode root = TreeNodeFileUtil.readEncodingTreeFromFile(encodedFileRandom);

//            int byte1 = encodedFileRandom.read();
//            int byte2 = encodedFileRandom.read();

            try (BitInputStream2 bitInStream = new BitInputStream2(encodedFileRandom)) {

                try (OutputStream out = Files.newOutputStream(decodedPath);
                     DataOutputStream decodedDataOut = new DataOutputStream(out)) {
                    while (true) {

                        TreeNode cur = root;

                        while (!cur.isLeaf()) {

                            int bitValue = bitInStream.read();

                            if (bitValue == -1) {
                                throw new IllegalStateException("Stream ended unexpectedly");
                            }

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

                        decodedDataOut.writeChar(cur.ch);
                    }
                }

                LOG.info(bitInStream);

            }
        }
        catch (IOException ioEx) {
            throw new IllegalStateException(ioEx);
        }
    }
}
