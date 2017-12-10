package com.daya.android.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Shnoble on 2017. 12. 10..
 */

public class AESCipher {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";;

    public static byte[] encrypt(byte[] plainText, byte[] key, byte[] iv) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(byte[] cipherText, byte[] key, byte[] iv) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] generateKey(int keySize) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(keySize);
            return keyGenerator.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] generateIv(int size) {
        byte[] iv = new byte[size];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        IvParameterSpec spec = new IvParameterSpec(iv);
        return spec.getIV();
    }
}
