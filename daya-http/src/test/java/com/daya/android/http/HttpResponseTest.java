package com.daya.android.http;

import org.junit.Test;

import java.net.HttpURLConnection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by shhong on 2017. 9. 19..
 */
public class HttpResponseTest {
    @Test
    public void testBuilder() throws Exception {
        HttpResponse response = new HttpResponse.Builder()
                .setBody("Hello world")
                .setCode(HttpURLConnection.HTTP_OK)
                .build();

        assertNotNull(response);

        assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
        assertEquals("Hello world", response.getBody());
    }
}

