package com.daya.android.http.request;

import android.support.annotation.NonNull;

import com.daya.android.http.HttpMethod;
import com.daya.android.http.HttpResponse;
import com.daya.android.utils.Validate;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by shhong on 2017. 9. 15..
 */

public abstract class HttpRequest {
    private URL mUrl;
    private String mMethod;

    HttpRequest(Builder builder) {
        this.mUrl = builder.mUrl;
        this.mMethod = builder.mMethod;
    }

    URL getUrl() {
        return mUrl;
    }

    String getMethod() {
        return mMethod;
    }

    public abstract HttpResponse execute();

    public static class Builder {
        private URL mUrl;
        private String mMethod;

        public Builder() {
            this.mMethod = HttpMethod.GET;
        }

        public Builder setUrl(@NonNull URL url) {
            Validate.notNull(url, "url cannot be null");

            this.mUrl = url;
            return this;
        }

        public Builder setUrl(@NonNull String url) throws MalformedURLException {
            Validate.notNull(url, "url cannot be null");

            this.mUrl = new URL(url);
            return this;
        }

        public Builder setMethod(String method) {
            this.mMethod = method;
            return this;
        }

        public HttpRequest build() {
            switch (mMethod) {
                case HttpMethod.GET:
                    return new HttpRequestGet(this);
                case HttpMethod.POST:
                    return new HttpRequestPost(this);
                default:
                    throw new UnsupportedOperationException(mMethod + " is not supported method");
            }
        }
    }
}
