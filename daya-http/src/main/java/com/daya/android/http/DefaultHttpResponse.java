package com.daya.android.http;

/**
 * Created by shhong on 2017. 9. 27..
 */

public class DefaultHttpResponse implements HttpResponse {

    private int mCode;
    private String mMessage;
    private String mBody;

    @Override
    public HttpResponse setCode(int code) {
        this.mCode = code;
        return this;
    }

    @Override
    public HttpResponse setMessage(String message) {
        this.mMessage = message;
        return this;
    }

    @Override
    public HttpResponse setBody(String body) {
        this.mBody = body;
        return this;
    }

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getBody() {
        return mBody;
    }
}
