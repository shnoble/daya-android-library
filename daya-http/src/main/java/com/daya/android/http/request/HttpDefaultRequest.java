package com.daya.android.http.request;

import android.support.annotation.NonNull;

import com.daya.android.http.HttpMethod;

import java.net.URL;

/**
 * Created by shhong on 2017. 9. 15..
 */

class HttpDefaultRequest implements HttpRequest {
    private URL mUrl;
    private String mMethod;

    HttpDefaultRequest() {
    }

    @NonNull
    @Override
    public URL getUrl() {
        return mUrl;
    }

    void setUrl(@NonNull URL url) {
        this.mUrl = url;
    }

    @NonNull
    @Override
    public String getMethod() {
        return mMethod;
    }

    void setMethod(@NonNull @HttpMethod.MethodName String method) {
        this.mMethod = method;
    }
}
