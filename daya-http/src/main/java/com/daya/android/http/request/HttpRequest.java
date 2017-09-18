package com.daya.android.http.request;

import android.support.annotation.NonNull;

import java.net.URL;

/**
 * Created by shhong on 2017. 9. 15..
 */

public interface HttpRequest {

    @NonNull
    public URL getUrl();

    @NonNull
    public String getMethod();
}
