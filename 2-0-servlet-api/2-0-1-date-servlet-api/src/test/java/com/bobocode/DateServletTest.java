package com.bobocode;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reflections.Reflections;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Set;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class DateServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletOutputStream outputStream;

    private Object dateServletObject;
    private Class<?> dateServletClass;

    public static final String SERVLET_PACKAGE = "com.bobocode.servlet";
    public static final String DATE_SERVLET = "DateServlet";
    Reflections reflections;

    @BeforeEach
    public void init() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException,
            InstantiationException {
        reflections = new Reflections(SERVLET_PACKAGE);
        dateServletClass = Class.forName(SERVLET_PACKAGE + "." + DATE_SERVLET);
        dateServletObject = dateServletClass.getConstructors()[0].newInstance();
    }

    @Test
    @Order(1)
    void dateServletClassExists() throws ClassNotFoundException {
        Class.forName(SERVLET_PACKAGE + "." + DATE_SERVLET);
    }

    @Test
    @Order(2)
    void dateServletExtendsHttpServlet() {

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
    void dateServletIsMarkedAsWebServlet() {
        Set<Class<?>> servlets =
                reflections.getTypesAnnotatedWith(WebServlet.class);
        servlets.stream()
                .map(Class::getSimpleName)
                .filter(servlet -> servlet.equals(DATE_SERVLET))
                .findAny()
                .orElseThrow();
    }

    @Test
    @Order(4)
    void dateServletContentTypeIsProper() throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, IOException {
        Method doGetMethod = getDoGetMethod();

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        doGetMethod.invoke(dateServletObject, request, response);
        verify(response).setContentType("text/html");
    }

    @Test
    @Order(5)
    void dateServletReturnsDateInResponse() throws IOException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
        Method doGetMethod = getDoGetMethod();

        doGetMethod.invoke(dateServletObject, request, response);
        assertThat(stringWriter.getBuffer().toString().contains(LocalDate.now().toString())).isTrue();
    }

    private Method getDoGetMethod() throws NoSuchMethodException {
        return dateServletClass
                .getMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
    }
}