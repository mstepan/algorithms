package com.max.algs;

import com.google.common.io.BaseEncoding;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.time.Instant;

public final class EncryptionUtils {

    /*


    loginId=abcd@paymentus.com;firstName=;lastName=;email=;accounts=7881973462|UTILITY;timestamp=1552401571442;

    https://secure2.paymentus.com/cp/orcl/sso-login.action?authToken=C78C81C7D980CC7D89BE9F0241ADFF223CE00160B6CEC812A59A9457E1D27202EA49533331BD626780F9AD9EA1D37A450CCE3C9763E830175D421674C5F0A686B537BFA83DA7C51B8F32BAA8056DEE1F33C2A369397E93C821002F2D3A5FF85797F50EF3CA4230862BCF89DB082D000E72DF3D852EF878BF3424A6D90F8A29AEC045D52B7EA762BA7E5AD98670A32E7C

     */

    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_ALGORITHM = "AES";
    private static final int KEY_LENGTH = 128;
    private static final int VECTOR_PART_LENGTH = KEY_LENGTH / 8;
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String SEC_PROVIDER = "SunJCE";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static void main(String[] args) {
        final String issuedAt = String.valueOf(Instant.now().toEpochMilli());
        final String ttl = String.valueOf(360 * 1000);

        System.out.println(createEncryptedToken("accountId=7881973462;email=abcd@paymentus.com;firstName=Test;lastName=Paymentus" +
                                                        "issuedAt=" + issuedAt + ";ttl=" + ttl,
                                                "5EB0468F6220A6AD2A6BFAFF6450FFFF"));

        /*

        C78C81C7D980CC7D89BE9F0241ADFF223CE00160B6CEC812A59A9457E1D27202EA49533331BD626780F9AD9EA1D37A450CCE3C9763E830175D421674C5F0A686B537BFA83DA7C51B8F32BAA8056DEE1F33C2A369397E93C821002F2D3A5FF85797F50EF3CA4230862BCF89DB082D000E72DF3D852EF878BF3424A6D90F8A29AEC045D52B7EA762BA7E5AD98670A32E7C

         */
    }

    public static String createEncryptedToken(String source, String key) {

        try {
            return createToken(source, key);
        }
        catch (UnsupportedEncodingException | GeneralSecurityException e) {
            throw new IllegalArgumentException("Could not create token", e);
        }
    }

    private static String createToken(String source, String key) throws UnsupportedEncodingException,
            GeneralSecurityException {

        byte[] inputVector = new byte[VECTOR_PART_LENGTH];

        SECURE_RANDOM.nextBytes(inputVector);
        IvParameterSpec randomInputVector = new IvParameterSpec(inputVector);

        byte[] buffer = source.getBytes(DEFAULT_ENCODING);
        Cipher cipher = createCipher(CIPHER_ALGORITHM);
        SecretKey secretKey = createSecretKey(key, KEY_ALGORITHM);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, randomInputVector);
        byte[] encrypted = cipher.doFinal(buffer);
        byte[] encryptedVectorAndData = new byte[inputVector.length + encrypted.length];
        System.arraycopy(inputVector, 0, encryptedVectorAndData, 0, inputVector.length);
        System.arraycopy(encrypted, 0, encryptedVectorAndData, inputVector.length, encrypted.length);
        return BaseEncoding.base16().encode(encryptedVectorAndData).toUpperCase();
    }

    private static Cipher createCipher(String cipherAlgorithm) {
        try {
            return Cipher.getInstance(cipherAlgorithm, SEC_PROVIDER);
        }
        catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("Could not create cipher", e);
        }
    }

    private static SecretKey createSecretKey(String hexAesKey, String keyAlgorithm) {
        try {
            return getSecretKey(hexAesKey, keyAlgorithm);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Could not create secret key", e);
        }
    }

    private static SecretKey getSecretKey(String hexAesKey, String algorithm) {
        byte[] key = BaseEncoding.base16().decode(hexAesKey);
        return new SecretKeySpec(key, algorithm);
    }
}
