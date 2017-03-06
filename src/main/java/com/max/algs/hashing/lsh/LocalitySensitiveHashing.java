package com.max.algs.hashing.lsh;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;


public class LocalitySensitiveHashing {

    private static final String HASHING_ALGORITHM = "SHA-256";

    private static final int HASH_SIZE = 256; // use 256 bits because SHA-256 hash is 256 bits long

    private static final int DEFAULT_TABLES_COUNT = 8;

    private final LocalityHashBucket[] buckets;

    public LocalitySensitiveHashing() {
        buckets = new LocalityHashBucket[DEFAULT_TABLES_COUNT];

        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new LocalityHashBucket(HASH_SIZE);
        }
    }


    public void add(int[] data) {

        String hash = simhash(data);

        for (LocalityHashBucket bucket : buckets) {
            bucket.add(hash, data);
        }

    }

    /**
     * Find similar value base of specified threshold.
     *
     * @param searchData          - search data set
     * @param similarityThreshold - similarityFromSimhash threshold to be used
     * @return first data set which satisfies similarityFromSimhash threshold.
     */
    public int[] search(int[] searchData, double similarityThreshold) {

        checkArgument(isSimilarityCorrect(similarityThreshold));

        String hash = simhash(searchData);

        for (LocalityHashBucket bucket : buckets) {
            List<int[]> similarValues = bucket.get(hash);

            if (similarValues != null) {

                for (int[] other : similarValues) {

                    double similarityCoeff = similarityFromSimhash(searchData, other);

                    System.out.println("similarityCoeff = " + similarityCoeff);

                    if (Double.compare(similarityCoeff, similarityThreshold) >= 0) {
                        System.out.println("used similarityCoeff = " + similarityCoeff);
                        return other;
                    }
                }
            }
        }

        return null;
    }

    private static boolean isSimilarityCorrect(double similarity) {
        return Double.compare(similarity, 0.0) >= 0 && Double.compare(similarity, 1.0) <= 0;
    }


    /**
     * Find most similar value.
     */
    public int[] search(int[] searchData) {

        String hash = simhash(searchData);

        int[] mostSimilar = null;
        double maxSimilarityCoeff = 0.0;

        for (LocalityHashBucket bucket : buckets) {
            List<int[]> similarValues = bucket.get(hash);

            if (similarValues != null) {

                for (int[] other : similarValues) {

                    double similarityCoeff = similarityFromSimhash(searchData, other);

                    System.out.println("similarityCoeff = " + similarityCoeff);

                    if (Double.compare(similarityCoeff, maxSimilarityCoeff) >= 0) {
                        mostSimilar = other;
                        maxSimilarityCoeff = similarityCoeff;
                    }
                }
            }
        }

        return mostSimilar;
    }


    /*
    * Simhash algorithm:
    *
    * 1. Define a fingerprint size (for instance 256 bits)
    * 2. Create an array counters[] filled with this size of zeros
    * 3. For each element in the dataset, we create a unique hash with sha256,
    * or any other hash algorithm that give same-sized results
    * 4. For each hash, for each bit i in this hash:
    *       If the bit is 0, we add 1 to counters[i]
    *       If the bit is 1, we take 1 from counters[i]
    * 5. For each bit j of the global fingerprint:
    *       If counters[j] >= 0, we set counters[j] = 1
    *       If counters[j] < 0, we set counters[j] = 0
    *
    * It gives us a fingerprint characterizing our text, an approximation of the text data.
    * This fingerprint is a binary number, for instance: 10101011100010001010000101111100.
    */
    private static String simhash(int[] data) {

        MessageDigest hashingInstance = getDigest(HASHING_ALGORITHM);

        int[] counters = new int[HASH_SIZE];

        for (int value : data) {
            for (int i = 0; i < Integer.BYTES; i++) {
                hashingInstance.update((byte) (value & 0xFF));
                value >>>= Byte.SIZE;
            }
            updateCountersFromDigest(counters, hashingInstance.digest());
        }

        return buildHashFromCounters(counters);
    }

    private static void updateCountersFromDigest(int[] counters, byte[] digest) {

        int byteIndex = -1;

        byte curByte = 0;

        for (int index = 0; index < HASH_SIZE; index++) {

            if ((index % Byte.SIZE) == 0) {
                ++byteIndex;
                curByte = digest[byteIndex];
            }

            int bitValue = curByte & 0x01;

            // If the bit is 0, we add 1 to V[i]
            // If the bit is 1, we take 1 from V[i]
            if (bitValue == 0) {
                ++counters[index];
            }
            else {
                --counters[index];
            }

            curByte >>= 1;
        }
    }

    private static String buildHashFromCounters(int[] counters) {
        StringBuilder hash = new StringBuilder(128);

        for (int singleCounter : counters) {
            if (singleCounter >= 0) {
                hash.append("1");
            }
            else {
                hash.append("0");
            }
        }

        return hash.toString();
    }

    private static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        }
        catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private static double similarityFromSimhash(int[] arr1, int[] arr2) {
        String hash1 = simhash(arr1);
        String hash2 = simhash(arr2);

        final int hashLength = hash1.length();

        return 1.0 - ((double) hammingDistance(hash1, hash2) / hashLength);
    }

    private static int hammingDistance(String hash1, String hash2) {

        int distance = 0;
        for (int i = 0; i < hash1.length(); i++) {
            if (hash1.charAt(i) != hash2.charAt(i)) {
                ++distance;
            }
        }
        return distance;
    }


}
