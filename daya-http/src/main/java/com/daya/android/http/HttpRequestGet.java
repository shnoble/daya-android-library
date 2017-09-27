package com.daya.android.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by shhong on 2017. 9. 27..
 */

public class HttpRequestGet extends AbstractHttpRequest {
    private HttpRequestGet(@NonNull Builder builder) {
        super(builder);
    }

    @NonNull
    @Override
    public String getMethod() {
        return HttpMethod.GET;
    }

    @Nullable
    @Override
    public String getBody() {
        return null;
    }

    public static class Builder extends AbstractHttpRequest.Builder<HttpRequestGet> {

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
