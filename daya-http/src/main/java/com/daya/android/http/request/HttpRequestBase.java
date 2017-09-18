package com.daya.android.http.request;

/**
 * Created by shhong on 2017. 9. 15..
 */

abstract class HttpRequestBase {
    /*private HttpURLConnection mConnection;

    HttpRequestBase(@NonNull @HttpMethod String method,
                    @NonNull URL url) throws HttpRequestException {
        try {
            this.mConnection = (HttpURLConnection) url.openConnection();
            this.mConnection.setRequestMethod(method);
        } catch (Exception e) {
            throw new HttpRequestException(e);
        }
    }

    HttpRequestBase(@NonNull @HttpMethod String method,
                    @NonNull String url)
            throws IOException {
        this(method, new URL(url));
    }

    @NonNull
    public URL getUrl() {
        return mConnection.getURL();
    }

    @NonNull
    public String getMethod() {
        return mConnection.getRequestMethod();
    }

    public abstract HttpResponse execute();*/
}
