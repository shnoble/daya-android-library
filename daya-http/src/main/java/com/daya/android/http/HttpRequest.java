package com.daya.android.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.daya.android.util.Validate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shhong on 2017. 9. 15..
 */

public abstract class HttpRequest {
    /**
     * 'UTF-8' charset name
     */
    public static final String CHARSET_UTF8 = "UTF-8";

    /**
     * 'application/x-www-form-urlencoded' content type header value
     */
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

    /**
     * 'application/json' content type header value
     */
    public static final String CONTENT_TYPE_JSON = "application/json";

    /**
     * 'Content-Length' header name
     */
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";

    /**
     * 'Content-Type' header name
     */
    public static final String HEADER_CONTENT_TYPE = "Content-Type";

    private final URL mUrl;
    private final int mReadTimeout;
    private final int mConnectTimeout;
    private final Map<String, String> mHeaders;
    private final String mBody;

    @SuppressWarnings("unchecked")
    HttpRequest(@NonNull Builder builder) {
        this.mUrl = builder.mUrl;
        this.mReadTimeout = builder.mReadTimeout;
        this.mConnectTimeout = builder.mConnectTimeout;
        this.mHeaders = (builder.mHeaders != null) ? new HashMap<>(builder.mHeaders) : null;
        this.mBody = builder.mBody;
    }

    @NonNull
    public URL getUrl() {
        return mUrl;
    }

    @NonNull
    public abstract String getMethod();

    public int getReadTimeout() {
        return mReadTimeout;
    }

    public int getConnectTimeout() {
        return mConnectTimeout;
    }

    @Nullable
    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    @Nullable
    public String getBody() {
        return mBody;
    }

    public static abstract class Builder<T extends HttpRequest> {
        private URL mUrl;
        private int mReadTimeout = 3000;
        private int mConnectTimeout = 3000;
        private Map<String, String> mHeaders;
        private String mBody;

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
        public Builder<T> setBody(String body) {
            this.mBody = body;
            return this;
        }

        public abstract T build();
    }
}
