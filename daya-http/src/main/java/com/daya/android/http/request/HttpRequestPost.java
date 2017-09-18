package com.daya.android.http.request;

import com.daya.android.http.HttpResponse;

/**
 * Created by shhong on 2017. 9. 18..
 */

class HttpRequestPost extends HttpRequest {
    HttpRequestPost(Builder builder) {
        super(builder);
    }

    @Override
    public HttpResponse execute() {
        return null;
    }
    /*HttpRequestPost(@NonNull URL url) throws IOException {
        super(HttpMethod.POST, url);
    }

    HttpRequestPost(@NonNull String url) throws IOException {
        super(HttpMethod.GET, url);
    }

    @Override
    public HttpResponse execute() {
        return null;
    }*/
}
