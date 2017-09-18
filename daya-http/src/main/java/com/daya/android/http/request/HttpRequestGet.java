package com.daya.android.http.request;

import com.daya.android.http.HttpResponse;

/**
 * Created by shhong on 2017. 9. 18..
 */

class HttpRequestGet extends HttpRequest {
    HttpRequestGet(Builder builder) {
        super(builder);
    }

    @Override
    public HttpResponse execute() {
        return null;
    }

    /*HttpRequestGet(@NonNull URL url) throws IOException {
        super(HttpMethod.GET, url);
    }

    HttpRequestGet(@NonNull String url) throws IOException {
        super(HttpMethod.GET, url);
    }

    @Override
    public HttpResponse execute() {
        return null;
    }*/
}
