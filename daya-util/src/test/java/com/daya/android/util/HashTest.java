package com.daya.android.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by shhong on 2017. 10. 16..
 */
public class HashTest {
    private static final String INPUT_MESSAGE = "MD5 Algorithm Test";
    private static final String MD5_RESULT_VALUE = "fc635627982a901f26f484eb5add3ef1";
    private static final String SHA1_RESULT_VALUE = "735c37877234060a0547a3e899ccc0cc018d798e";

    @Test
    public void testMd5() throws Exception {
        String result = Hash.md5(INPUT_MESSAGE);
        assertEquals(result, MD5_RESULT_VALUE);
    }

    @Test
    public void testSha1() throws Exception {
        String result = Hash.sha1(INPUT_MESSAGE);
        assertEquals(result, SHA1_RESULT_VALUE);
    }
}

