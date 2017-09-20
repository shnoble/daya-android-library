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

    /**
     * Sets up a connection and gets the HTTP response body from the server.
     * If the network request is successful, it returns the response. Otherwise,
     * it will throw an IOException.
     *
     * @return returns the response.
     */
    @WorkerThread
    @NonNull
    @Override
    public HttpResponse execute() throws IOException {
        return null;
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
