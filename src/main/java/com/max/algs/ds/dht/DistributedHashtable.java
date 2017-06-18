package com.max.algs.ds.dht;

import com.max.algs.ds.dht.cache_node.CacheNode;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class DistributedHashtable implements AutoCloseable {

    private static final Logger LOG = Logger
            .getLogger(DistributedHashtable.class);

    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");

    private static final int REPLICAS_COUNT = 3;

    private static final Random RAND = ThreadLocalRandom.current();
    private static final int RING_LENGTH = 360;
    private final MessageDigest messageDigest;
    private final NavigableMap<Integer, Node> ring = new TreeMap<>(); // 'ring'
    // of
    // buckets

    private final Map<String, List<Integer>> replicas = new HashMap<>(); // track
    // url
    // =>
    // bucket
    // indexes

    private final HttpClient httpClient = new DefaultHttpClient();

    private int nodesCount; // 'logical' nodes count without replicas

    private int size;

    public DistributedHashtable() {
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        }
        catch (NoSuchAlgorithmException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void addNode(String urlStr) {
        try {

            startCacheServer("node-" + nodesCount, urlStr);

            List<Integer> replicasIndexes = new ArrayList<>();

            Node newNode = new Node(urlStr, httpClient);

            for (int i = 0; i < REPLICAS_COUNT; i++) {

                int bucketIndex = -1;

                while (bucketIndex < 0 || ring.containsKey(bucketIndex)) {
                    bucketIndex = nodeIndex(urlStr + "_" + i) % RING_LENGTH;
                }

                replicasIndexes.add(bucketIndex);

                ring.put(bucketIndex, newNode);

                checkRemapping(newNode, bucketIndex);

            }

            replicas.put(urlStr, replicasIndexes);

            ++nodesCount;
        }
        catch (Exception ex) {
            LOG.error(ex);
        }
    }

    private void checkRemapping(Node newNode, int bucketIndex) {

        // don't do any remapping if table is empty or just one logical node
        // presented
        if (size == 0 || nodesCount <= 1) {
            return;
        }

        Node nextNode = nextNode(bucketIndex);

        // don't do remappping for the same node replicas
        if (newNode.equals(nextNode)) {
            return;
        }

        Map.Entry<Integer, Node> prevEntry = prevEntry(bucketIndex);

        // remap range of keys
        nextNode.remap(newNode, prevEntry.getKey() + 1, bucketIndex);
    }

    private void startCacheServer(String name, String urlStr)
            throws URISyntaxException {
        URI nodeUrl = new URI(urlStr);
        new Thread(new CacheNode(name, nodeUrl.getPort())).start();
    }

    public void put(String key, String value) {
        boolean wasAdded = findNode(key).put(key, value, bucketIndex(key));

        if (wasAdded) {
            ++size;
        }
    }

    public String get(String key) {
        return findNode(key).getValue(key);
    }

    public boolean contains(String key) {
        return findNode(key).getValue(key) != null;
    }

    public boolean remove(String key) {
        boolean wasDeleted = findNode(key).remove(key);

        if (wasDeleted) {
            --size;
        }

        return wasDeleted;
    }

    private Node findNode(String key) {

        int index = bucketIndex(key);

        for (Map.Entry<Integer, Node> entry : ring.entrySet()) {
            if (entry.getKey() >= index) {
                return entry.getValue();
            }
        }

        return ring.get(ring.firstKey());
    }

    private int nodeIndex(String url) {
        String urlWithPrefix = new String(messageDigest.digest((url + RAND
                .nextInt()).getBytes()));
        return bucketIndex(urlWithPrefix);
    }

    private int bucketIndex(String key) {
        return Math.abs(key.hashCode() & 0x7F_FF_FF_FF) % RING_LENGTH;
    }

    public void removeNode(String url) {

        List<Integer> replicaIndexes = replicas.get(url);

        for (int replicaToRemoveIndex : replicaIndexes) {

            Node nodeToRemove = ring.get(replicaToRemoveIndex);

            Node nextNode = nextNode(replicaToRemoveIndex);

            /** merge data from previous node, handle all corner cases here */
            nextNode.mergeData(nodeToRemove);

            ring.remove(replicaToRemoveIndex);
        }

    }

    private Map.Entry<Integer, Node> prevEntry(int index) {

        --index;

        if (index < 0) {
            index = RING_LENGTH - 1;
        }

        Map.Entry<Integer, Node> prevEntry = ring.floorEntry(index);

        if (prevEntry == null) {
            return ring.lastEntry();
        }

        return prevEntry;
    }

    private Node nextNode(int index) {

        Map.Entry<Integer, Node> nextEntry = ring.ceilingEntry((index + 1)
                % RING_LENGTH);

        if (nextEntry == null) {
            if (ring.firstEntry() != null) {
                return ring.firstEntry().getValue();
            }

            return null;
        }

        return nextEntry.getValue();
    }

    @SuppressWarnings("unused")
    private Node findNodeByIndex(int index) {

        Map.Entry<Integer, Node> entry = ring.ceilingEntry(index);

        if (entry == null) {
            if (ring.firstEntry() != null) {
                return ring.firstEntry().getValue();
            }

            return null;
        }

        return entry.getValue();

    }

    @Override
    public void close() throws Exception {
        httpClient.getConnectionManager().shutdown();
    }

    @Override
    public String toString() {

        StringBuilder buf = new StringBuilder();

        for (Map.Entry<Integer, Node> entry : ring.entrySet()) {
            buf.append(entry.getKey() + "=>" + entry.getValue().getUrl())
                    .append(LINE_SEPARATOR);
        }

        return buf.toString();
    }

}
