package com.daya.android.http;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.daya.android.util.Validate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * Created by shhong on 2017. 9. 18..
 */

public class HttpClient {

    @WorkerThread
    public static HttpResponse execute(@NonNull HttpRequest request) throws IOException {
        return execute(request, DefaultHttpResponse.class);
    }

    /**
     * Sets up a connection and gets the HTTP response body from the server.
     * If the network request is successful, it returns the response. Otherwise,
     * it will throw an IOException.
     *
     * @return returns the response.
     */
    @WorkerThread
    public static <T extends HttpResponse> T execute(@NonNull HttpRequest request,
                                                     @NonNull Class<T> classOfT)
            throws IOException {
        Validate.runningOnWorkerThread(
                HttpClient.class.getCanonicalName()
                        + "#execute(HttpRequestOld) method should be called from the worker thread");
        Validate.notNull(request, "request cannot be null");

        OutputStream outputStream = null;
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        T response = null;

        try {
            connection = (HttpURLConnection) request.getUrl().openConnection();

            // Set timeout for reading InputStream
            connection.setReadTimeout(request.getReadTimeout());

            // Set timeout for connection.connect()
            connection.setConnectTimeout(request.getConnectTimeout());

            // Set HTTP method
            connection.setRequestMethod(request.getMethod());

            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);

            if (!HttpMethod.GET.equals(request.getMethod())) {
                connection.setDoOutput(true);
            }

            // Set request property
            Map<String, String> requestHeaders = request.getHeaders();
            if (requestHeaders != null) {
                for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                    connection.setRequestProperty(header.getKey(), header.getValue());
                }
            }

            // Open communications link (network traffic occurs here).
            connection.connect();

            String requestBody = request.getBody();
            if (requestBody != null) {
                outputStream = connection.getOutputStream();
                if (outputStream != null) {
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, HttpRequest.CHARSET_UTF8));
                    bufferedWriter.write(requestBody);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                }
            }

            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            String responseBody = null;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Retrieve the response body as an InputStream.
                inputStream = connection.getInputStream();
                if (inputStream != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder responseBodyBuilder = new StringBuilder();
                    String inputLine;
                    while ((inputLine = bufferedReader.readLine()) != null) {
                        responseBodyBuilder.append(inputLine);
                    }
                    responseBody = responseBodyBuilder.toString();
                }
            }

            try {
                response = classOfT.newInstance();
                response.setCode(responseCode)
                        .setMessage(responseMessage)
                        .setBody(responseBody);

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        } finally {
            try {
                // Close InputStream
                if (inputStream != null) {
                    inputStream.close();
                }

                // Close OutputStream
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                // Ignored
                e.printStackTrace();
            }

            // Disconnect connection.
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response;
    }
}
