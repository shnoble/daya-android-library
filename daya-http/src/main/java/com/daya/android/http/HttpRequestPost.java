package com.daya.android.http;

import android.support.annotation.NonNull;

/**
 * Created by shhong on 2017. 9. 18..
 */

public class HttpRequestPost extends HttpRequest {
    private HttpRequestPost(@NonNull Builder builder) {
        super(builder);
    }

    @NonNull
    @Override
    public String getMethod() {
        return HttpMethod.POST;
    }

    public static class Builder extends HttpRequest.Builder<HttpRequestPost> {

        @NonNull
        @Override
        public HttpRequestPost build() {
            return new HttpRequestPost(this);
        }
    }
}
