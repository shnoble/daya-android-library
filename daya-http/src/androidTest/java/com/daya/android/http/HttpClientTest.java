package com.daya.android.http;

import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Shnoble on 2017. 9. 19..
 */
public class HttpClientTest {
    private static final String HTTP_GET_URL = "https://httpbin.org/get";
    private static final String HTTP_POST_URL = "https://httpbin.org/post";
    private static final String HTTP_GET_INVALID_HOST_URL = "https://invalid-httpbin.org/get";
    private static final String HTTP_POST_INVALID_HOST_URL = "https://invalid-httpbin.org/post";
    private static final String HTTP_GET_INVALID_URL = "https://httpbin.org/get/invalid";
    private static final String HTTP_POST_INVALID_URL = "https://httpbin.org/post/invalid";

    @Test
    public void testHttpGet() throws Exception {
        HttpRequest request = new HttpRequestGet.Builder()
                .setUrl(HTTP_GET_URL)
                .setReadTimeout(5000)
                .setConnectTimeout(5000)
                .setHeader("content-type", "application/json")
                .build();

        assertNotNull(request);

        DefaultHttpResponse response = HttpClient.execute(request, DefaultHttpResponse.class);
        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
        assertNotNull(response.getMessage());
        assertNotNull(response.getBody());
    }

    @Test
    public void testHttpPost() throws Exception {
        HttpRequest request = new HttpRequestPost.Builder()
                .setUrl(HTTP_POST_URL)
                .setReadTimeout(5000)
                .setConnectTimeout(5000)
                .setHeader("content-type", "application/json")
                .setBody("Hello world")
                .build();
        assertNotNull(request);

        DefaultHttpResponse response = HttpClient.execute(request, DefaultHttpResponse.class);
        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
        assertNotNull(response.getMessage());
        assertNotNull(response.getBody());
    }

    @Test(expected = UnknownHostException.class)
    public void testHttpGetWithInvalidHost() throws Exception {
        HttpRequest request = new HttpRequestGet.Builder()
                .setUrl(HTTP_GET_INVALID_HOST_URL)
                .setReadTimeout(5000)
                .setConnectTimeout(5000)
                .setHeader("content-type", "application/json")
                .build();

        assertNotNull(request);
        HttpClient.execute(request);
    }

    @Test(expected = UnknownHostException.class)
    public void testHttpPostWithInvalidHost() throws Exception {
        HttpRequest request = new HttpRequestPost.Builder()
                .setUrl(HTTP_POST_INVALID_HOST_URL)
                .setReadTimeout(5000)
                .setConnectTimeout(5000)
                .setHeader("content-type", "application/json")
                .setBody("Hello world")
                .build();
        assertNotNull(request);

        HttpClient.execute(request);
    }

    @Test
    public void testHttpGetWithInvalidUrl() throws Exception {
        HttpRequest request = new HttpRequestGet.Builder()
                .setUrl(HTTP_GET_INVALID_URL)
                .setReadTimeout(5000)
                .setConnectTimeout(5000)
                .setHeader("content-type", "application/json")
                .build();

        assertNotNull(request);

        DefaultHttpResponse response = HttpClient.execute(request, DefaultHttpResponse.class);
        assertNotNull(response);
        assertNotEquals(HttpURLConnection.HTTP_OK, response.getCode());
        assertNotNull(response.getMessage());
        assertNull(response.getBody());
    }

    @Test
    public void testHttpPostWithInvalidUrl() throws Exception {
        HttpRequest request = new HttpRequestPost.Builder()
                .setUrl(HTTP_POST_INVALID_URL)
                .setReadTimeout(5000)
                .setConnectTimeout(5000)
                .setHeader("content-type", "application/json")
                .setBody("Hello world")
                .build();
        assertNotNull(request);

        DefaultHttpResponse response = HttpClient.execute(request, DefaultHttpResponse.class);
        assertNotNull(response);
        assertNotEquals(HttpURLConnection.HTTP_OK, response.getCode());
        assertNotNull(response.getMessage());
        assertNull(response.getBody());
    }
}
