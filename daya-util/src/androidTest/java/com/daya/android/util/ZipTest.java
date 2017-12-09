package com.daya.android.util;

import android.util.Base64;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by shhong on 2017. 12. 8..
 */
public class ZipTest {
    private static final String TEST_STRING = "Hello world!!";
    private static final String COMPRESSED_STRING = "eJzzSM3JyVcozy/KSVFUBAAhiAR/";

    @Test
    public void testCompress() throws Exception {
        byte[] compressedBytes = Zip.compress(TEST_STRING.getBytes("UTF-8"));
        String result = Base64.encodeToString(compressedBytes, Base64.NO_WRAP);
        assertEquals(COMPRESSED_STRING, result);
    }

    @Test
    public void testDecompress() throws Exception {
        byte[] compressedBytes = Base64.decode(COMPRESSED_STRING, Base64.NO_WRAP);
        byte[] result = Zip.decompress(compressedBytes);
        assertEquals(TEST_STRING, new String(result));
    }
}