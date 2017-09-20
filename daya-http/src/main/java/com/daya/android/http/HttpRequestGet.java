package com.daya.android.http;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import java.io.IOException;

/**
 * Created by shhong on 2017. 9. 18..
 */

public class HttpRequestGet extends HttpRequest {
    private HttpRequestGet(@NonNull Builder builder) {
        super(builder);
    }

    @NonNull
    @Override
    public String getMethod() {
        return HttpMethod.GET;
    }

    public static class Builder extends HttpRequest.Builder<HttpRequestGet> {

        @NonNull
        @Override
        public Builder setBody(String body) {
            throw new UnsupportedOperationException("Not supported for HTTP GET");
        }

        @NonNull
        @Override
        public HttpRequestGet build() {
            return new HttpRequestGet(this);
        }
    }
}
