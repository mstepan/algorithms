package com.max.algs.effectivejava;

import org.apache.log4j.Logger;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

final class EffectiveJavaMain {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private static final class StringListGood implements java.io.Serializable {

        private transient Node root;
        private transient Node tail;
        private transient int size;

        private static final class Node implements java.io.Serializable {
            final String value;
            Node next;
            Node prev;

            public Node(String value) {
                this.value = value;
            }
        }

        public void add(String value) {
            if (root == null) {
                root = new Node(value);
                tail = root;
            }
            else {

                tail.next = new Node(value);
                tail.next.prev = tail;
                tail = tail.next;
            }
            ++size;
        }

        /**
         * Serialize this {@code StringListGood} instance.
         *
         * @serialData The size of this list instance serialized.
         * Followed by all string values.
         */
        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();

            out.writeInt(size);

            for (Node cur = root; cur != null; cur = cur.next) {
                out.writeUTF(cur.value);
            }
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();

            int totalSize = in.readInt();

            for (int i = 0; i < totalSize; ++i) {
                add(in.readUTF());
            }
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();

            buf.append("[").append(root.value);

            for (Node cur = root.next; cur != null; cur = cur.next) {
                buf.append(", ").append(cur.value);
            }

            buf.append("]");
            return buf.toString();
        }
    }


    private static final class StringListBad implements java.io.Serializable {

        private Node root;
        private Node tail;
        private int size;

        private static final class Node implements java.io.Serializable {
            final String value;
            Node next;
            Node prev;

            public Node(String value) {
                this.value = value;
            }
        }

        public void add(String value) {
            if (root == null) {
                root = new Node(value);
                tail = root;
            }
            else {
                tail.next = new Node(value);
                tail.next.prev = tail;
                tail = tail.next;
            }
            ++size;
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();

            buf.append("[").append(root.value);

            for (Node cur = root.next; cur != null; cur = cur.next) {
                buf.append(", ").append(cur.value);
            }

            buf.append("]");
            return buf.toString();
        }

    }

    private EffectiveJavaMain() throws Exception {

        StringListGood goodList = new StringListGood();
        goodList.add("hello");
        goodList.add("world");
        goodList.add("some");
        goodList.add("additional");
        goodList.add("value");

        Path goodPath =
                Paths.get("/Users/mstepan/repo/algorithms/src/main/java/com/max/algs/effectivejava/encoded-good.bin");

        writeObject(goodList, goodPath);
        readObject(goodPath);


        StringListBad badList = new StringListBad();
        badList.add("hello");
        badList.add("world");
        badList.add("some");
        badList.add("additional");
        badList.add("value");

        Path badPath =
                Paths.get("/Users/mstepan/repo/algorithms/src/main/java/com/max/algs/effectivejava/encoded-bad.bin");

        writeObject(badList, badPath);
        readObject(badPath);

        LOG.info("java version: " + System.getProperty("java.version"));
    }

    private static void writeObject(Object obj, Path path) throws IOException {

        Files.deleteIfExists(path);
        Files.createFile(path);

        try (OutputStream out = Files.newOutputStream(path);
             ObjectOutputStream objOut = new ObjectOutputStream(out)) {
            objOut.writeObject(obj);
        }

    }

    private static void readObject(Path path) throws IOException, ClassNotFoundException {
        try (InputStream in = Files.newInputStream(path);
             ObjectInputStream objIn = new ObjectInputStream(in)) {
            LOG.info(objIn.readObject());
        }
    }

    public static void main(String[] args) {
        try {
            new EffectiveJavaMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
