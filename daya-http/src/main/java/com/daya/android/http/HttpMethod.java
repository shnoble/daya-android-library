package com.daya.android.http;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by shhong on 2017. 9. 15..
 */

public class HttpMethod {
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({GET, POST})
    public @interface MethodName {}

    static final String GET = "GET";
    static final String POST = "POST";
}
