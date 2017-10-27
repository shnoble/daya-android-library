package com.shnoble.http.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.daya.android.http.DefaultHttpResponse;
import com.daya.android.http.HttpClient;
import com.daya.android.http.HttpRequest;
import com.daya.android.http.HttpRequestGet;
import com.daya.android.http.HttpRequestPost;

import java.io.IOException;

import static junit.framework.Assert.assertNotNull;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String HTTP_GET_URL = "https://httpbin.org/get";
    private static final String HTTP_POST_URL = "https://httpbin.org/post";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSend(View view) {
        Log.d(TAG, "Send button was clicked.");

        switch (view.getId()) {
            case R.id.send_get_button:
                sendGet();
                break;
            case R.id.send_post_button:
                sendPost();
                break;
        }
    }

    private void sendGet() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    HttpRequest request = new HttpRequestGet.Builder()
                            .setUrl(HTTP_GET_URL)
                            .setReadTimeout(5000)
                            .setConnectTimeout(5000)
                            .setHeader("content-type", "application/json")
                            .build();

                    Log.d(TAG, "Request: \n"
                            + "- url: " + request.getUrl() + "\n"
                            + "- readTimeout: " + request.getReadTimeout() + "\n"
                            + "- connectTimeout: " + request.getConnectTimeout() + "\n"
                            + "- headers: " + request.getHeaders());


                    DefaultHttpResponse response = HttpClient.execute(request, DefaultHttpResponse.class);
                    Log.d(TAG, "Response: \n"
                            + "- code: " + response.getCode() + "\n"
                            + "- body: " + response.getBody());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    private void sendPost() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    HttpRequest request = new HttpRequestPost.Builder()
                            .setUrl(HTTP_POST_URL)
                            .setReadTimeout(5000)
                            .setConnectTimeout(5000)
                            .setHeader("content-type", "application/json")
                            .setBody("Hello world")
                            .build();
                    assertNotNull(request);

                    Log.d(TAG, "Request: \n"
                            + "- url: " + request.getUrl() + "\n"
                            + "- readTimeout: " + request.getReadTimeout() + "\n"
                            + "- connectTimeout: " + request.getConnectTimeout() + "\n"
                            + "- headers: " + request.getHeaders() + "\n"
                            + "- body: " + request.getBody());

                    DefaultHttpResponse response = HttpClient.execute(request, DefaultHttpResponse.class);
                    Log.d(TAG, "Response: \n"
                            + "- code: " + response.getCode() + "\n"
                            + "- body: " + response.getBody());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
}

