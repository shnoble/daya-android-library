package com.daya.android.http;

import android.support.annotation.NonNull;

import com.daya.android.http.request.HttpRequest;
import com.daya.android.http.request.HttpRequestFactory;

import java.io.IOException;
import java.net.URL;

/**
 * Created by shhong on 2017. 9. 18..
 */

public class HttpClient {
    @NonNull
    public static HttpRequest newRequestGet(@NonNull URL url) throws IOException {
        return HttpRequestFactory.newRequestGet(url);
    }

    @NonNull
    public static HttpRequest newRequestGet(@NonNull String url) throws IOException {
        return HttpRequestFactory.newRequestGet(url);
    }
}
