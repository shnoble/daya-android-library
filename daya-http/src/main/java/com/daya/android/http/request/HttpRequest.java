package com.daya.android.http.request;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import com.daya.android.utils.Validate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by shhong on 2017. 9. 15..
 */

public interface HttpRequest {
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({METHOD_GET, METHOD_POST})
    public @interface Method {}

    /**
     * 'GET' request method
     */
    static final String METHOD_GET = "GET";

    /**
     * 'POST' request method
     */
    static final String METHOD_POST = "POST";

    public URL getUrl();

    @Method
    public String getMethod();

    class Builder {
        private URL mUrl;
        private String mMethod;

        public Builder setUrl(@NonNull String url) throws MalformedURLException {
            this.mUrl = new URL(url);
            return this;
        }


        public Builder setMethod(@NonNull @Method String method) {
            this.mMethod = method;
            return this;
        }

        public HttpRequest build() {
            Validate.notNull(mUrl, "URL cannot be null.");
            Validate.notNull(mMethod, "Request method cannot be null.");

            DefaultHttpRequest request = new DefaultHttpRequest();
            request.setUrl(mUrl);
            request.setMethod(mMethod);
            return request;
        }
    }
}
