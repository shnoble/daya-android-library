package com.daya.android.http.request;

import com.daya.android.http.HttpMethod;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Shnoble on 2017. 9. 18..
 */
public class HttpRequestBuilderTest {
    private static final String HTTP_URL = "http://localhost";

    @Test
    public void testBuilder() throws Exception {
        HttpRequest request = new HttpRequest.Builder()
                .setUrl(HTTP_URL)
                .setMethod(HttpMethod.GET)
                .build();

        assertNotNull(request);
        assertNotNull(request.getUrl());

        assertEquals(HTTP_URL, request.getUrl().toString());
        assertEquals(HttpMethod.GET, request.getMethod());
    }

    @Test(expected = NullPointerException.class)
    public void occurExceptionIfUrlIsNull() throws Exception {
        new HttpRequest.Builder()
                .setUrl((URL) null);
    }

    @Test(expected = MalformedURLException.class)
    public void occurExceptionIfUrlIsInvalid() throws Exception {
        new HttpRequest.Builder()
                .setUrl("abcd");
    }

    @Test(expected = NullPointerException.class)
    public void occurExceptionIfUrlIsNullString() throws Exception {
        new HttpRequest.Builder()
                .setUrl((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void occurExceptionIfMethodIsEmptyString() throws Exception {
        new HttpRequest.Builder()
                .setMethod("");
    }

    @Test(expected = NullPointerException.class)
    public void occurExceptionIfMethodIsNull() throws Exception {
        new HttpRequest.Builder()
                .setMethod(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void occurExceptionIfMethodIsInvalid() throws Exception {
        new HttpRequest.Builder()
                .setMethod("SHOW");
    }
}

