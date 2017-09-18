package com.daya.android.http;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.daya.android.http.HttpMethod.GET;
import static com.daya.android.http.HttpMethod.POST;

/**
 * Created by shhong on 2017. 9. 18..
 */

@Retention(RetentionPolicy.SOURCE)
@StringDef({GET, POST})
public @interface HttpMethod {
    /**
     * 'GET' request method
     */
    static final String GET = "GET";

    /**
     * 'POST' request method
     */
    static final String POST = "POST";
}
