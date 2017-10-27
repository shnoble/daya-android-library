package com.daya.android.http;

/**
 * Created by shhong on 2017. 9. 27..
 */

public interface HttpResponse {

    HttpResponse setCode(int code);

    HttpResponse setMessage(String message);

    HttpResponse setBody(String body);
}

