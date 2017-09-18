package com.daya.android.http.request;

import com.daya.android.http.HttpMethod;
import com.daya.android.http.HttpResponse;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by shhong on 2017. 9. 18..
 */
public class HttpRequestBaseTest {

    private HttpRequest newInstance(String method, URL url) throws Exception {
        return new HttpRequestBase(method, url) {
            @Override
            public HttpResponse execute() {
                return null;
            }
        };
    }

    private HttpRequest newInstance(String method, String url) throws Exception {
        return new HttpRequestBase(method, url) {
            @Override
            public HttpResponse execute() {
                return null;
            }
        };
    }

    @Test
    public void testGetUrl() throws Exception {
        HttpRequest request = newInstance(HttpMethod.GET, "https://httpbin.org/get");
        assertNotNull(request);
        assertNotNull(request.getUrl());
        assertEquals("https://httpbin.org/get", request.getUrl().toString());
    }

    @Test(expected = IOException.class)
    public void occurExceptionIfUrlIsNull() throws Exception {
        newInstance(HttpMethod.GET, (URL) null);
    }

    @Test(expected = IOException.class)
    public void occurExceptionIfUrlIsInvalid() throws Exception {
        newInstance(HttpMethod.GET, "abcd");
    }

    @Test(expected = IOException.class)
    public void occurExceptionIfUrlIsNullString() throws Exception {
        newInstance(HttpMethod.GET, (String) null);
    }

    @Test(expected = IOException.class)
    public void occurExceptionIfUrlIsEmptyString() throws Exception {
        newInstance(HttpMethod.GET, "");
    }

    @Test
    public void testGetMethod() throws Exception {
        HttpRequest requestGet = newInstance(HttpMethod.GET, "https://httpbin.org/get");
        assertNotNull(requestGet);
        assertNotNull(requestGet.getMethod());
        assertEquals(HttpMethod.GET, requestGet.getMethod());

        HttpRequest requestPost = newInstance(HttpMethod.POST, "https://httpbin.org/get");
        assertNotNull(requestPost);
        assertNotNull(requestPost.getMethod());
        assertEquals(HttpMethod.POST, requestPost.getMethod());
    }
}

