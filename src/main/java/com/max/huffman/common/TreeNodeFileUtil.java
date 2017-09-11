package com.max.huffman.common;

import org.apache.log4j.Logger;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;

public final class TreeNodeFileUtil {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private TreeNodeFileUtil() {
    }

    public static void writeEncodingTreeToFile(TreeNode root, Path outPath) {
        try {
            try (OutputStream fileOutStream = Files.newOutputStream(outPath);
                 BufferedOutputStream bufferedOutStream = new BufferedOutputStream(fileOutStream);
                 DataOutputStream dataOut = new DataOutputStream(bufferedOutStream)) {

                dataOut.writeInt(root.size);

                writeNodeRec(root, dataOut);
            }
        }
        catch (IOException ioEx) {
            LOG.error("Can't write encoding tree to compressed file", ioEx);
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


    public static TreeNode readEncodingTreeFromFile(Path outPath) {
        try {
            try (InputStream fileInStream = Files.newInputStream(outPath);
                 BufferedInputStream bufInStream = new BufferedInputStream(fileInStream);
                 DataInputStream dataOut = new DataInputStream(bufInStream)) {

                int treeSize = dataOut.readInt();
                int charsCount = 0;

                Deque<TreeNode> stack = new ArrayDeque<>();

                TreeNode root = TreeNode.createLeaf(dataOut.readChar(), 0);
                stack.push(root);

                for (int i = 0; i < treeSize - 1; ++i) {

                    char ch = dataOut.readChar();

                    if (ch != Character.MIN_VALUE) {
                        ++charsCount;
                    }



                }

                LOG.info("Unique chars count (de-compression): " + charsCount);

                return root;
            }
        }
        catch (IOException ioEx) {
            throw new IllegalStateException("Can't read encoding tree from compressed file", ioEx);
        }
    }

}
