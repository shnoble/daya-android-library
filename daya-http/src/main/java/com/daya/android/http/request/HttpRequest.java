package com.daya.android.http.request;

import android.support.annotation.NonNull;

import com.daya.android.http.HttpMethod;
import com.daya.android.utils.Validate;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by shhong on 2017. 9. 15..
 */

public interface HttpRequest {

    public URL getUrl();

    public String getMethod();

    class Builder {
        private URL mUrl;
        private String mMethod;

        public Builder setUrl(@NonNull String url) throws MalformedURLException {
            this.mUrl = new URL(url);
            return this;
        }


        public Builder setMethod(@NonNull @HttpMethod.MethodName String method) {
            this.mMethod = method;
            return this;
        }

        public HttpRequest build() {
            Validate.notNull(mUrl, "URL cannot be null.");
            Validate.notNull(mMethod, "Request method cannot be null.");

            HttpDefaultRequest request = new HttpDefaultRequest();
            request.setUrl(mUrl);
            request.setMethod(mMethod);
            return request;
        }
    }
}
