package com.daya.android.http;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Shnoble on 2017. 9. 18..
 */
public class HttpRequestTest {
    private static final String HTTP_URL = "http://localhost";

    @Test
    public void testBuilder() throws Exception {
        HttpRequest.Builder builder = new HttpRequest.Builder() {
            @Override
            public HttpRequest build() {
                return null;
            }
        }.setUrl(HTTP_URL)
                .setReadTimeout(5000)
                .setConnectTimeout(5000)
                .setHeader("content-type", "application/json")
                .setBody("Hello world");

        assertNotNull(builder);
    }

    @Test(expected = NullPointerException.class)
    public void occurExceptionIfUrlIsNull() throws Exception {
        new HttpRequest.Builder() {
            @Override
            public HttpRequest build() {
                return null;
            }
        }.setUrl((URL) null);
    }

    @Test(expected = MalformedURLException.class)
    public void occurExceptionIfUrlIsInvalid() throws Exception {
        new HttpRequest.Builder() {
            @Override
            public HttpRequest build() {
                return null;
            }
        }.setUrl("abcd");
    }

    @Test(expected = NullPointerException.class)
    public void occurExceptionIfUrlIsNullString() throws Exception {
        new HttpRequest.Builder() {
            @Override
            public HttpRequest build() {
                return null;
            }
        }.setUrl((String) null);
    }
}

