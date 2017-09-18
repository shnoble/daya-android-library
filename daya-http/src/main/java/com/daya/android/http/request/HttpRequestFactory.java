package com.daya.android.http.request;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.net.URL;

/**
 * Created by shhong on 2017. 9. 18..
 */

public class HttpRequestFactory {
    public static HttpRequest newRequestGet(@NonNull URL url) throws IOException {
        return new HttpRequestGet(url);
    }

    public static HttpRequest newRequestGet(@NonNull String url) throws IOException {
        return new HttpRequestGet(url);
    }

    public static HttpRequest newRequestPost(@NonNull URL url) throws IOException {
        return new HttpRequestPost(url);
    }

    public static HttpRequest newRequestPost(@NonNull String url) throws IOException {
        return new HttpRequestPost(url);
    }
}
