package com.daya.android.http;

import android.support.annotation.NonNull;

import com.daya.android.utils.Validate;

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
    public static HttpResponse execute(@NonNull HttpRequest request) throws IOException {
        Validate.notNull(request, "request is cannot null");

        OutputStream outputStream = null;
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        HttpResponse response = null;

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

            response = new HttpResponse.Builder()
                    .setCode(responseCode)
                    .setBody(responseBody)
                    .build();

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
