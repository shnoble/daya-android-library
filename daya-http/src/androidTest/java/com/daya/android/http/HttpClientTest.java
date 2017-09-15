package com.daya.android.http;

import com.daya.android.http.request.HttpRequest;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by shhong on 2017. 9. 15..
 */
public class HttpClientTest {
    private static final String SERVER_GET_URL = "https://httpbin.org/get";

    @Test
    public void checkMethodDefinition() throws Exception {
        Assert.assertEquals("GET", HttpRequest.METHOD_GET);
        Assert.assertEquals("POST", HttpRequest.METHOD_POST);
    }

    @Test(expected = NullPointerException.class)
    public void occurExceptionIfMethodNotSet() throws Exception {
        new HttpRequest.Builder()
                .setUrl("https://httpbin.org/get")
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void occurExceptionIfUrlNotSet() throws Exception {
        new HttpRequest.Builder()
                .setMethod(HttpRequest.METHOD_GET)
                .build();
    }

    @Test
    public void createRequestObject() throws Exception {
        HttpRequest request = new HttpRequest.Builder()
                .setUrl("https://httpbin.org/get")
                .setMethod(HttpRequest.METHOD_GET)
                .build();

        Assert.assertNotNull(request);
    }

    @Test
    public void requestGetMethod() throws Exception {
        HttpClient httpClient = new HttpClient();

        HttpRequest request = new HttpRequest.Builder()
                .setUrl(SERVER_GET_URL)
                .setMethod(HttpRequest.METHOD_GET)
                .build();

        Assert.assertEquals(HttpRequest.METHOD_GET, request.getMethod());
        Assert.assertEquals(SERVER_GET_URL, request.getUrl().toString());

        HttpResponse response = httpClient.execute(request);
        Assert.assertNotNull(response);
    }

    @Test
    public void post() throws Exception {
//        HttpClient httpClient = new HttpClient();
//
//        HttpRequest request = new HttpRequestPost("http://");
//
//        HttpResponse response = httpClient.execute(request);
//        Assert.assertNotNull(response);
    }
}