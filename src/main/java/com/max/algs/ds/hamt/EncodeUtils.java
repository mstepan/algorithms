package com.max.algs.ds.hamt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public final class EncodeUtils {

    private static final int ZERO_CH = '0';

    private static final int UNSIGNED_BYTE_MAX_VALUE = 255;

    private static final String HASH_ALGORITHM = "SHA-256";

    private static final String[] byteToStringMap = new String[UNSIGNED_BYTE_MAX_VALUE+1];

    private static MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance(HASH_ALGORITHM);
        }
        catch (NoSuchAlgorithmException ex) {
            throw new ExceptionInInitializerError("Can't find hashing algorithm '" + HASH_ALGORITHM + "': " + ex.getMessage());
        }

        for(int i = 0; i < byteToStringMap.length; i++){
            byteToStringMap[i] = byteToBinaryString(i);
        }
    }

    private EncodeUtils(){}

    public static String byteToBinaryString(int value) {

        StringBuilder buf = new StringBuilder(Byte.SIZE);

        for (int i = 0; i < Byte.SIZE; i++) {
            buf.append((char) (ZERO_CH + (value & 1)));
            value >>= 1;
        }

        buf.reverse();

        return buf.toString();
    }


    public static String hashKey(String key) {
        try {
            byte[] data = digest.digest(key.getBytes("UTF-8"));

            StringBuilder buf = new StringBuilder(data.length * Byte.SIZE);

            for (int i = 0; i < data.length; i++) {
                buf.append(byteToStringMap[data[i] & 0xFF]);
            }

            return buf.toString();
        }
        catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }
    }

}
