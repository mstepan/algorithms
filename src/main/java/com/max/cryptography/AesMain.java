package com.max.cryptography;


import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class AesMain {

    private static final Logger LOG = Logger.getLogger(AesMain.class);

    private static final byte[] INITIAL_VECTOR = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();

    private AesMain() throws Exception {

        String str = "133";

        IvParameterSpec ivSpec = new IvParameterSpec(INITIAL_VECTOR);
        SecretKey key = generateKey();

        LOG.info("Initial: " + str);

        String encoded = encode(str, key, ivSpec);
        LOG.info("Encoded: " + encoded);

        String decoded = decode(encoded, key, ivSpec);
        LOG.info("Decoded: " + decoded);

        LOG.info("AesMain done... java-" + System.getProperty("java.version"));
    }

    private static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    private static String encode(String value, SecretKey key, IvParameterSpec ivSpec) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] encryptedData = cipher.doFinal(value.getBytes());

        return new String(BASE64_ENCODER.encode(encryptedData));
    }

    private static String decode(String value, SecretKey key, IvParameterSpec ivSpec) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] encodedData = cipher.doFinal(BASE64_DECODER.decode(value));

        return new String(encodedData);
    }


    public static void main(String[] args) {
        try {
            new AesMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
