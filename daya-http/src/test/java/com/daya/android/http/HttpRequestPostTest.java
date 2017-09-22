package com.daya.android.http;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by shhong on 2017. 9. 19..
 */
public class HttpRequestPostTest {
    private static final String HTTP_URL = "https://httpbin.org/post";

    @Test
    public void testBuilder() throws Exception {
        HttpRequestPost request = new HttpRequestPost.Builder()
                .setUrl(HTTP_URL)
                .setReadTimeout(5000)
                .setConnectTimeout(5000)
                .setHeader("content-type", "application/json")
                .setBody("Hello world")
                .build();

        assertNotNull(request);
        assertNotNull(request.getUrl());
        assertNotNull(request.getHeaders());

        assertEquals(HTTP_URL, request.getUrl().toString());
        assertEquals(HttpMethod.POST, request.getMethod());
        assertEquals(5000, request.getReadTimeout());
        assertEquals(5000, request.getConnectTimeout());
        assertEquals("application/json", request.getHeaders().get("content-type"));
        assertEquals("Hello world", request.getBody());
    }
}

