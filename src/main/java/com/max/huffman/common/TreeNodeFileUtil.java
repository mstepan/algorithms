package com.max.huffman.common;

import org.apache.log4j.Logger;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;

public final class TreeNodeFileUtil {

    public static final char END_MARKER = Character.MAX_VALUE;

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

            assert node.left != null : "node.left == null";
            assert node.right != null : "node.right == null";

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

                Deque<TreeNode> stack = new ArrayDeque<>();

                TreeNode root = TreeNode.createLeaf(dataOut.readChar(), 0);
                stack.push(root);

                for (int i = 0; i < treeSize - 1; ++i) {

                    char ch = dataOut.readChar();

                    TreeNode curNode = TreeNode.createLeaf(ch, 0);
                    TreeNode stackTopNode = stack.peekFirst();

                    if (stackTopNode.left == null) {
                        stackTopNode.left = curNode;
                    }
                    else if (stackTopNode.right == null) {
                        stackTopNode.right = curNode;

                        TreeNode completedNode = stack.pop();

                        assert completedNode.left != null && completedNode.right != null : "node not completed";
                    }
                    else {
                        assert true : "should never happen";
                    }

                    // push only non leaf nodes
                    if (ch == Character.MIN_VALUE) {
                        stack.push(curNode);
                    }
                }

                assert stack.isEmpty() : "stack not empty";

                return root;
            }
        }
        catch (IOException ioEx) {
            throw new IllegalStateException("Can't read encoding tree from compressed file", ioEx);
        }
    }

}
