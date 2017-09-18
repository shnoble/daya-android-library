package com.daya.android.http;

import com.daya.android.http.request.HttpRequest;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Shnoble on 2017. 9. 19..
 */
public class HttpClientTest {
    private static final String HTTP_GET_URL = "https://httpbin.org/get";
    private static final String HTTP_POST_URL = "https://httpbin.org/post";

    @Test
    public void testHttpGet() throws Exception {
        HttpRequest request = new HttpRequest.Builder()
                .setMethod(HttpMethod.GET)
                .setUrl(HTTP_GET_URL)
                .build();
        assertNotNull(request);

        HttpResponse response = HttpClient.execute(request);
        assertNotNull(response);
    }

    @Test
    public void testHttpPost() throws Exception {
        HttpRequest request = new HttpRequest.Builder()
                .setMethod(HttpMethod.POST)
                .setUrl(HTTP_POST_URL)
                .build();
        assertNotNull(request);

        HttpResponse response = HttpClient.execute(request);
        assertNotNull(response);
    }
}

