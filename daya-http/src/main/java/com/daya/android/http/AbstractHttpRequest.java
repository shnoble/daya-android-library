package com.daya.android.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.daya.android.util.Validate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shhong on 2017. 9. 27..
 */

abstract class AbstractHttpRequest implements HttpRequest {

    private final URL mUrl;
    private final int mReadTimeout;
    private final int mConnectTimeout;
    private final Map<String, String> mHeaders;

    @SuppressWarnings("unchecked")
    AbstractHttpRequest(@NonNull Builder builder) {
        this.mUrl = builder.mUrl;
        this.mReadTimeout = builder.mReadTimeout;
        this.mConnectTimeout = builder.mConnectTimeout;
        this.mHeaders = (builder.mHeaders != null) ? new HashMap<>(builder.mHeaders) : null;
    }

    @NonNull
    @Override
    public URL getUrl() {
        return mUrl;
    }

    @Override
    public int getReadTimeout() {
        return mReadTimeout;
    }

    @Override
    public int getConnectTimeout() {
        return mConnectTimeout;
    }

    @Nullable
    @Override
    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public static abstract class Builder<T extends AbstractHttpRequest> {
        private URL mUrl;
        private int mReadTimeout = 3000;
        private int mConnectTimeout = 3000;
        private Map<String, String> mHeaders;

        public Builder() {
        }

        @NonNull
        public Builder<T> setUrl(@NonNull URL url) {
            Validate.notNull(url, "url cannot be null");

            this.mUrl = url;
            return this;
        }

        @NonNull
        public Builder<T> setUrl(@NonNull String url) throws MalformedURLException {
            Validate.notNull(url, "url cannot be null");

            this.mUrl = new URL(url);
            return this;
        }

        @NonNull
        public Builder<T> setReadTimeout(int timeout) {
            this.mReadTimeout = timeout;
            return this;
        }

        @NonNull
        public Builder<T> setConnectTimeout(int timeout) {
            this.mConnectTimeout = timeout;
            return this;
        }

        @NonNull
        public Builder<T> setHeader(String key, String value) {
            if (this.mHeaders == null) {
                this.mHeaders = new HashMap<>();
            }
            this.mHeaders.put(key, value);
            return this;
        }

        @NonNull
        public abstract Builder<T> setBody(String body);

        @NonNull
        public abstract T build();
    }
}
