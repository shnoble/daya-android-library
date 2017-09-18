package com.daya.android.http;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by shhong on 2017. 9. 15..
 */
public class HttpRequestTest {
    private static final String SERVER_GET_URL = "https://httpbin.org/get";

    @Test
    public void checkMethodDefinition() throws Exception {
        Assert.assertEquals("GET", HttpMethod.GET);
        Assert.assertEquals("POST", HttpMethod.POST);
    }

    /*
    @Test(expected = HttpException.class)
    public void occurExceptionIfUrlIsNull() throws Exception {
        HttpGet httpGet = HttpRequest.newHttpGet((URL) null);
    }

    @Test(expected = HttpException.class)
    public void occurExceptionIfUrlIsInvalid() throws Exception {
        HttpGet httpGet = HttpRequest.newHttpGet("abcd");
    }

    @Test(expected = HttpException.class)
    public void occurExceptionIfUrlIsNullString() throws Exception {
        HttpGet httpGet = HttpRequest.newHttpGet((String) null);
    }

    @Test(expected = HttpException.class)
    public void occurExceptionIfUrlIsEmptyString() throws Exception {
        HttpGet httpGet = HttpRequest.newHttpGet("");
    }

    @Test
    public void createHttpGetObject() throws Exception {
        HttpGet httpGet = HttpRequest.newHttpGet("https://httpbin.org/get");
        Assert.assertNotNull(httpGet);
    }*/

    @Test
    public void requestGetMethod() throws Exception {
        //HttpResponse response = HttpRequest.newHttpGet("https://httpbin.org/get").execute();

//        HttpRequest httpClient = new HttpRequest();
//
//        HttpRequestOld request = new HttpRequestOld.Builder()
//                .setUrl(SERVER_GET_URL)
//                .setMethod(HttpMethod.GET)
//                .build();
//
//        Assert.assertEquals(HttpMethod.GET, request.getMethod());
//        Assert.assertEquals(SERVER_GET_URL, request.getUrl().toString());
//
//        HttpResponse response = httpClient.execute(request);
//        Assert.assertNotNull(response);
    }

    @Test
    public void post() throws Exception {
//        HttpRequest httpClient = new HttpRequest();
//
//        HttpRequestOld request = new HttpRequestPost("http://");
//
//        HttpResponse response = httpClient.execute(request);
//        Assert.assertNotNull(response);
    }
}