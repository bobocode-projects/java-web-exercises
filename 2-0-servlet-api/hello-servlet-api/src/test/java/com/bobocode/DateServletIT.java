package com.bobocode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class DateServletIT {
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
        assertThat(responseBuffer.toString().contains("Good job!")).isTrue();
    }

    @Test
    void dateServletResponseCodeIsOk() throws IOException {
        HttpURLConnection connection = servletConnection
                .getHttpURLConnectionGet("http://localhost:8080/date");
        int code = connection.getResponseCode();
        assertThat(code).isEqualTo(HttpURLConnection.HTTP_OK);
    }

    @Test
    void dateServletResponseHasDate() throws IOException {
        StringBuffer responseBuffer = servletConnection
                .sendGET("http://localhost:8080/date");
        String date = LocalDate.now().toString();
        System.out.println(date);
        assertThat(responseBuffer.toString().contains(date)).isTrue();
    }
}
