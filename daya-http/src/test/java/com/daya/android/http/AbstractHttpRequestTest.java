package com.daya.android.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Shnoble on 2017. 9. 18..
 */
public class AbstractHttpRequestTest {
    private static final String HTTP_URL = "http://localhost";

    private AbstractHttpRequest.Builder mBuilder;

    @Before
    public void setUp() throws Exception {
        mBuilder = new AbstractHttpRequest.Builder() {

            private String mBody;

            @NonNull
            @Override
            public AbstractHttpRequest.Builder setBody(String body) {
                mBody = body;
                return this;
            }

            @NonNull
            @Override
            public AbstractHttpRequest build() {
                return new AbstractHttpRequest(this) {

                    @NonNull
                    @Override
                    public String getMethod() {
                        return HttpMethod.POST;
                    }

                    @Nullable
                    @Override
                    public String getBody() {
                        return mBody;
                    }
                };
            }
        };
    }

    @Test
    public void testBuilder() throws Exception {
        HttpRequest request = mBuilder
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
    }

    @Test(expected = NullPointerException.class)
    public void occurExceptionIfUrlIsNull() throws Exception {
        mBuilder.setUrl((URL) null);
    }

    @Test(expected = MalformedURLException.class)
    public void occurExceptionIfUrlIsInvalid() throws Exception {
        mBuilder.setUrl("abcd");
    }

    @Test(expected = NullPointerException.class)
    public void occurExceptionIfUrlIsNullString() throws Exception {
        mBuilder.setUrl((String) null);
    }
}

