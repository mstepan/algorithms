package com.max.algs.hashing;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Different hash functions implementations.
 */
public final class HashUtils {

    private static final Random RAND = ThreadLocalRandom.current();
    private static final long FNV_OFFSET_32 = 2_166_136_26L;
    private static final int FNV_32_OFFSET_BASIS = (int) (FNV_OFFSET_32 ^ (FNV_OFFSET_32 >>> 32));
    private static final int FNV_32_PRIME = 16_777_619;
    private static final int MUR_MUR_C1 = 0xCC_9E_2D_51;
    private static final int MUR_MUR_C2 = 0x1B_87_35_93;
    private static final int MUR_MUR_R1 = 15;
    private static final int MUR_MUR_R2 = 13;
    private static final int MUR_MUR_M = 5;
    private static final int MUR_MUR_N = 0xE6_54_6B_64;
    private static final int MUR_MUR_SEED = RAND.nextInt();
    private HashUtils() {
        throw new IllegalStateException("Can't instantiate utility class");
    }

    /**
     * Standard jdk String hashing algorithm based on Horner's rule with base 31.
     */
    public static int standardHash(String str) {
        int hash = 0;

        for (int i = 0, length = str.length(); i < length; ++i) {
            hash = 31 * hash + str.charAt(i);
        }
        return hash;
    }

    /**
     * Jenkins one at a time hash.
     * http://en.wikipedia.org/wiki/Jenkins_hash_function
     */
    public static int jenkinsHash(String key) {

        final int keyLength = key.length();
        int hash = 0;

        for (int i = 0; i < keyLength; ++i) {
            hash += key.charAt(i);
            hash += (hash << 10);
            hash ^= (hash >> 6);
        }
        hash += (hash << 3);
        hash ^= (hash >> 11);
        hash += (hash << 15);

        return hash;
    }

    /**
     * Fowler–Noll–Vo hash function that produces 32 bits hash code.
     * See: http://www.isthe.com/chongo/tech/comp/fnv/index.html#FNV-source
     */
    public static int fnvHash(String str) {

        int hash = FNV_32_OFFSET_BASIS;

        for (int i = 0, length = str.length(); i < length; i++) {
            hash ^= str.charAt(i);
            hash *= FNV_32_PRIME;
        }

        return hash;
    }

    /**
     * Murmur 3 hashing algorithm.
     * See: https://en.wikipedia.org/wiki/MurmurHash
     */
    public static int murmur3Hash(String key) {

        int hash = MUR_MUR_SEED;

        int blocksCount = key.length() >> 2;

        for (int i = 0; i < blocksCount; i++) {
            hash = hashBucket(key, i >> 2, hash);
        }

        int index = blocksCount * 4;
        int k1 = 0;

        int maskedLength = key.length() & 3;

        // apply this transformation to key with length only '3'
        if (maskedLength == 3) {
            k1 ^= key.charAt(index + 2) << 16;
        }

        // apply this transformation to key with length '2' or '3'
        if (maskedLength >= 2) {
            k1 ^= key.charAt(index + 1) << 8;
        }

        // apply this transformation to key with length '1', '2' or '3'
        k1 ^= key.charAt(index);

        k1 *= MUR_MUR_C1;
        k1 = (k1 << MUR_MUR_R1) | (k1 >> (32 - MUR_MUR_R1));
        k1 *= MUR_MUR_C2;
        hash ^= k1;

        hash ^= key.length();
        hash ^= (hash >> 16);
        hash *= 0x85_EB_CA_6B;
        hash ^= (hash >> 13);
        hash *= 0xC2_B2_AE_35;
        hash ^= (hash >> 16);

        return hash;
    }


    private static int hashBucket(String key, int index, int hash) {

        int chunkHash = key.charAt(index) | (key.charAt(index + 1) << 8) |
                (key.charAt(index + 2) << 16) |
                (key.charAt(index + 3) << 24);

        chunkHash *= MUR_MUR_C1;
        chunkHash = (chunkHash << MUR_MUR_R1) | (chunkHash >> (32 - MUR_MUR_R1));
        chunkHash *= MUR_MUR_C2;

        hash ^= chunkHash;
        hash = ((hash << MUR_MUR_R2) | (hash >> (32 - MUR_MUR_R2))) * MUR_MUR_M + MUR_MUR_N;
        return hash;
    }


}
