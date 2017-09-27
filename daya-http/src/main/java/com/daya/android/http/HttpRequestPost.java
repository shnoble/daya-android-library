package com.daya.android.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by shhong on 2017. 9. 27..
 */

public class HttpRequestPost extends AbstractHttpRequest {

    private final String mBody;

    private HttpRequestPost(@NonNull Builder builder) {
        super(builder);

        this.mBody = builder.mBody;
    }

    @NonNull
    @Override
    public String getMethod() {
        return HttpMethod.POST;
    }

    @Nullable
    @Override
    public String getBody() {
        return mBody;
    }

    public static class Builder extends AbstractHttpRequest.Builder<HttpRequestPost> {

        private String mBody;

        @NonNull
        @Override
        public Builder setBody(String body) {
            this.mBody = body;
            return this;
        }

        @NonNull
        @Override
        public HttpRequestPost build() {
            return new HttpRequestPost(this);
        }
    }
}
