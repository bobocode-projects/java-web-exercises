package com.bobocode;

import org.junit.jupiter.api.*;
import org.reflections.Reflections;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.Set;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DateServletTest {
    public static final String SERVLET_PACKAGE = "com.bobocode.hello_servlet";
    public static final String DATE_SERVLET = "DateServlet";
    Reflections reflections;

    @BeforeEach
    public void init() {
        reflections = new Reflections(SERVLET_PACKAGE);
    }

    @Test
    @Order(1)
    void dateServletClassExisting() throws ClassNotFoundException {
        Class.forName(SERVLET_PACKAGE + "." + DATE_SERVLET);
    }

    @Test
    @Order(2)
    void dateServletIsHttpServlet() {

        Set<Class<? extends HttpServlet>> httpServlets =
                reflections.getSubTypesOf(HttpServlet.class);

        httpServlets.stream()
                .map(Class::getSimpleName)
                .filter(servlet -> servlet.equals(DATE_SERVLET))
                .findAny()
                .orElseThrow();
    }

    @Test
    @Order(3)
    void dateServletIsAnnotated() {
        Set<Class<?>> servlets =
                reflections.getTypesAnnotatedWith(WebServlet.class);
        servlets.stream()
                .map(Class::getSimpleName)
                .filter(servlet -> servlet.equals(DATE_SERVLET))
                .findAny()
                .orElseThrow();
    }
}