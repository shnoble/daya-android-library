package com.daya.android.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.URL;
import java.util.Map;

/**
 * Created by shhong on 2017. 9. 27..
 */

public interface HttpRequest {
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

    @NonNull
    URL getUrl();

    @NonNull
    String getMethod();

    int getReadTimeout();

    int getConnectTimeout();

    @Nullable
    Map<String, String> getHeaders();

    @Nullable
    String getBody();
}
