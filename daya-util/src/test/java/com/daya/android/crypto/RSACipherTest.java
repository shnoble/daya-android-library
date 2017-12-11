package com.daya.android.crypto;

import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Shnoble on 2017. 12. 10..
 */
public class RSACipherTest {
    private static final String PLAIN_TEXT = "Hello World";

    private byte[] mEncodedPublicKey;
    private byte[] mEncodedPrivateKey;

    @Before
    public void setUp() throws Exception {
        KeyPair keyPair = RSACipher.generateKeyPair();
        assertNotNull(keyPair);

        mEncodedPublicKey = RSACipher.encodePublicKey(keyPair.getPublic());
        mEncodedPrivateKey = RSACipher.encodePrivateKey(keyPair.getPrivate());
    }

    @Test
    public void testRSACipher() throws Exception {
        PublicKey publicKey = RSACipher.decodePublicKey(mEncodedPublicKey);
        assertNotNull(publicKey);

        byte[] encrypted = RSACipher.encrypt(PLAIN_TEXT.getBytes(), publicKey);
        assertNotNull(encrypted);

        PrivateKey privateKey = RSACipher.decodePrivateKey(mEncodedPrivateKey);
        assertNotNull(privateKey);

        byte[] decrypted = RSACipher.decrypt(encrypted, privateKey);
        assertNotNull(decrypted);

        assertEquals(PLAIN_TEXT, new String(decrypted));
    }
}