package com.daya.android.crypto;

import com.daya.android.util.Hex;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by shhong on 2017. 12. 11..
 */
public class AES128CipherTest {
    private static final String PLAIN_TEXT = "Hello world";
    private static final String CIPHER_TEXT_ECB_MODE = "b8e151b54a2c1146684c1f3cd507ed0e";
    private static final String CIPHER_TEXT_CBC_MODE = "d44abb667265cc898df66736627ecacf";
    private static final String KEY = "899979a67ef6292c153463170af9a983";
    private static final String IV = "8f33061456c8433900fa4ea0e322129d";

    @Test
    public void testGenerateKey() throws Exception {
        byte[] key = AES128Cipher.generateKey();
        assertNotNull(key);

        System.out.println("Key: " + Hex.bytesToHex(key));
    }

    @Test
    public void testGenerateIv() throws Exception {
        byte[] iv = AES128Cipher.generateIv();
        assertNotNull(iv);

        System.out.println("IV: " + Hex.bytesToHex(iv));
    }

    @Test
    public void testEncryptECBMode() throws Exception {
        byte[] cipherText = AES128Cipher.encrypt(PLAIN_TEXT.getBytes(), Hex.HexToBytes(KEY));
        assertNotNull(cipherText);

        System.out.println("Cipher Text: " + Hex.bytesToHex(cipherText));
        assertEquals(CIPHER_TEXT_ECB_MODE, Hex.bytesToHex(cipherText));
    }

    @Test
    public void testEncryptCBCMode() throws Exception {
        byte[] cipherText = AES128Cipher.encrypt(PLAIN_TEXT.getBytes(), Hex.HexToBytes(KEY), Hex.HexToBytes(IV));
        assertNotNull(cipherText);

        System.out.println("Cipher Text: " + Hex.bytesToHex(cipherText));
        assertEquals(CIPHER_TEXT_CBC_MODE, Hex.bytesToHex(cipherText));
    }

    @Test
    public void testDecryptECBMode() throws Exception {
        byte[] cipherText = Hex.HexToBytes(CIPHER_TEXT_ECB_MODE);
        byte[] plainText = AES128Cipher.decrypt(cipherText, Hex.HexToBytes(KEY));
        assertNotNull(plainText);

        System.out.println("Plain Text: " + Hex.bytesToHex(plainText));
        assertEquals(PLAIN_TEXT, new String(plainText));
    }

    @Test
    public void testDecryptCBCMode() throws Exception {
        byte[] cipherText = Hex.HexToBytes(CIPHER_TEXT_CBC_MODE);
        byte[] plainText = AES128Cipher.decrypt(cipherText, Hex.HexToBytes(KEY), Hex.HexToBytes(IV));
        assertNotNull(plainText);

        System.out.println("Plain Text: " + Hex.bytesToHex(plainText));
        assertEquals(PLAIN_TEXT, new String(plainText));
    }
}