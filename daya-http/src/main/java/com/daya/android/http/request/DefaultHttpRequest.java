package com.daya.android.http.request;

import android.support.annotation.NonNull;

import java.net.URL;

/**
 * Created by shhong on 2017. 9. 15..
 */

class DefaultHttpRequest implements HttpRequest {
    private URL mUrl;
    private String mMethod;

    DefaultHttpRequest() {
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
    @Method
    @Override
    public String getMethod() {
        return mMethod;
    }

    void setMethod(@NonNull @Method String method) {
        this.mMethod = method;
    }
}
