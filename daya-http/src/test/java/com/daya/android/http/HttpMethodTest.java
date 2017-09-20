package com.daya.android.http;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by shhong on 2017. 9. 19..
 */
public class HttpMethodTest {
    @Test
    public void testMethodDefinition() throws Exception {
        Assert.assertEquals("GET", HttpMethod.GET);
        Assert.assertEquals("POST", HttpMethod.POST);
    }
}

