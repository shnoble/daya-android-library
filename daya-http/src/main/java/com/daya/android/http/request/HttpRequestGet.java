package com.daya.android.http.request;

import android.support.annotation.NonNull;

import com.daya.android.http.HttpMethod;
import com.daya.android.http.HttpResponse;

import java.io.IOException;
import java.net.URL;

/**
 * Created by shhong on 2017. 9. 18..
 */

class HttpRequestGet extends HttpRequestBase {
    HttpRequestGet(@NonNull URL url) throws IOException {
        super(HttpMethod.GET, url);
    }

    HttpRequestGet(@NonNull String url) throws IOException {
        super(HttpMethod.GET, url);
    }

    @Override
    public HttpResponse execute() {
        return null;
    }
}
