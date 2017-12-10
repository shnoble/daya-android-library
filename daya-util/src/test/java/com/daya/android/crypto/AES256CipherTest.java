package com.daya.android.crypto;

import com.daya.android.util.Hex;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Shnoble on 2017. 12. 11..
 */
public class AES256CipherTest {
    private static final String PLAIN_TEXT = "Hello TOAST Client Develop Team ~ !";
    private static final String CIPHER_TEXT = "";
    private static final String KEY = "5ba40d1bdc4fedc97ca2d16c24301f6ee952b8086ed1ded5cd216fd3ec116ddb";
    private static final String IV = "a68641536f3911370c868960a57f244c";

    @Test
    public void testGenerateKey() throws Exception {
        byte[] key = AES256Cipher.generateKey();
        assertNotNull(key);

        System.out.println("Key: " + Hex.bytesToHex(key));
    }

    @Test
    public void testGenerateIv() throws Exception {
        byte[] iv = AES256Cipher.generateIv();
        assertNotNull(iv);

        System.out.println("IV: " + Hex.bytesToHex(iv));
    }

    @Test
    public void testEncrypt() throws Exception {
        byte[] cipherText = AES256Cipher.encrypt(PLAIN_TEXT.getBytes(), Hex.HexToBytes(KEY), Hex.HexToBytes(IV));
        assertNotNull(cipherText);

        System.out.println("Cipher Text: " + Hex.bytesToHex(cipherText));
    }

    @Test
    public void testDecrypt() throws Exception {
        byte[] plainText = AES256Cipher.decrypt(CIPHER_TEXT.getBytes(), Hex.HexToBytes(KEY), Hex.HexToBytes(IV));
        assertNotNull(plainText);

        System.out.println("Plain Text: " + Hex.bytesToHex(plainText));
    }
}