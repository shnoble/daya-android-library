package com.daya.android.http;

import android.support.annotation.NonNull;

/**
 * Created by shhong on 2017. 9. 15..
 */

public class HttpResponse {
    private final int mCode;
    private final String mMessage;
    private final String mBody;

    private HttpResponse(@NonNull Builder builder) {
        this.mCode = builder.mCode;
        this.mMessage = builder.mMessage;
        this.mBody = builder.mBody;
    }

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getBody() {
        return mBody;
    }

    public static class Builder {
        private int mCode;
        private String mMessage;
        private String mBody;

        public Builder setCode(int code) {
            this.mCode = code;
            return this;
        }

        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        public Builder setBody(String body) {
            this.mBody = body;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }
}
