package com.daya.android.http.request;

import android.support.annotation.NonNull;

import com.daya.android.http.HttpMethod;
import com.daya.android.http.HttpResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by shhong on 2017. 9. 15..
 */

abstract class HttpRequestBase implements HttpRequest {
    private HttpURLConnection mConnection;

    HttpRequestBase(@NonNull @HttpMethod String method,
                    @NonNull URL url) throws HttpRequestException {
        try {
            this.mConnection = (HttpURLConnection) url.openConnection();
            this.mConnection.setRequestMethod(method);
        } catch (Exception e) {
            throw new HttpRequestException(e);
        }
    }

    HttpRequestBase(@NonNull @HttpMethod String method,
                    @NonNull String url)
            throws IOException {
        this(method, new URL(url));
    }

    @NonNull
    public URL getUrl() {
        return mConnection.getURL();
    }

    @NonNull
    public String getMethod() {
        return mConnection.getRequestMethod();
    }

    public abstract HttpResponse execute();
}
