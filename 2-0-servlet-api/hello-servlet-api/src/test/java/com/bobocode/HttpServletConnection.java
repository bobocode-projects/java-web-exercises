package com.bobocode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpServletConnection {
    private static final String USER_AGENT = "Mozilla/5.0";

    public StringBuffer sendGET(String url) throws IOException {
        HttpURLConnection connection = getHttpURLConnection(url);
        int responseCode = connection.getResponseCode();
        StringBuffer response = null;
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }
        return response;
    }

    public HttpURLConnection getHttpURLConnection(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        return connection;
    }
}
