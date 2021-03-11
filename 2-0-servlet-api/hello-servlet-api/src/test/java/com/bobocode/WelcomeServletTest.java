package com.bobocode;

import com.bobocode.welcome_servlet_web.WelcomeServlet;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.net.HttpURLConnection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class WelcomeServletTest {
    HttpServletConnection servletConnection = new HttpServletConnection();

    @Test
    void contextResponseCodeIsOk() throws IOException {
        HttpURLConnection connection = servletConnection.getHttpURLConnection("http://localhost:8080/");
        int code = connection.getResponseCode();
        assertThat(code).isEqualTo(HttpURLConnection.HTTP_OK);
    }

    @Test
    void requestWhenNameParameterIsAdded() throws IOException {
        StringBuffer responseBuffer = servletConnection.sendGET();
        assertThat(responseBuffer.toString().contains("Hello!")).isTrue();
    }
}
