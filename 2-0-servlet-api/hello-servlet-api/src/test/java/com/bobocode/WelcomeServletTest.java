package com.bobocode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class WelcomeServletTest {
    HttpServletConnection servletConnection = new HttpServletConnection();

    @Test
    void contextResponseCodeIsOk() throws IOException {
        HttpURLConnection connection = servletConnection.getHttpURLConnectionGet("http://localhost:8080/");
        int code = connection.getResponseCode();
        assertThat(code).isEqualTo(HttpURLConnection.HTTP_OK);
    }

    @Test
    void contextResponseHasHello() throws IOException {
        StringBuffer responseBuffer = servletConnection.sendGET("http://localhost:8080/");
        assertThat(responseBuffer.toString().contains("Hello!")).isTrue();
    }

    @Test
    void servletResponseCodeIsOk() throws IOException {
        HttpURLConnection connection = servletConnection
                .getHttpURLConnectionGet("http://localhost:8080/hello-servlet");
        int code = connection.getResponseCode();
        assertThat(code).isEqualTo(HttpURLConnection.HTTP_OK);
    }

    @Test
    void servletResponseWhenParameterIsPresent() throws IOException {
        StringBuffer responseBuffer = servletConnection
                .sendGET("http://localhost:8080/hello-servlet?name=Bob");
        assertThat(responseBuffer.toString().contains("Bob")).isTrue();
    }
}
