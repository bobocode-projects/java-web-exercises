package com.bobocode;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reflections.Reflections;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class DateServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

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
    @DisplayName("DateServlet class exists")
    void dateServletClassExists() throws ClassNotFoundException {
        Class.forName(SERVLET_PACKAGE + "." + DATE_SERVLET);
    }

    @Test
    @Order(2)
    @DisplayName("DateServlet class extends HttpServlet")
    void dateServletExtendsHttpServlet() {

        Set<Class<? extends HttpServlet>> httpServlets =
                reflections.getSubTypesOf(HttpServlet.class);

        Optional<String> anyDateHttpServlet = httpServlets.stream()
                .map(Class::getSimpleName)
                .filter(servlet -> servlet.equals(DATE_SERVLET))
                .findAny();
        assertThat(anyDateHttpServlet).isNotEmpty();
    }

    @Test
    @Order(3)
    @DisplayName("DateServlet class is marked as @WebServlet")
    void dateServletIsMarkedAsWebServlet() {
        Set<Class<?>> servlets =
                reflections.getTypesAnnotatedWith(WebServlet.class);
        Optional<String> anyMarkedDateServlet = servlets.stream()
                .map(Class::getSimpleName)
                .filter(servlet -> servlet.equals(DATE_SERVLET))
                .findAny();
        assertThat(anyMarkedDateServlet).isNotEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("DateServlet is mapped with \"/date\" path")
    void dateServletIsMarkedWithProperPath() throws ClassNotFoundException {
        String[] value = Class.forName(SERVLET_PACKAGE + "." + DATE_SERVLET)
                .getAnnotation(WebServlet.class).value();
        assertThat(value).contains("/date");
    }

    @Test
    @Order(5)
    @DisplayName("DateServlet returns current date in a response")
    void dateServletReturnsDateInResponse() throws IOException, InvocationTargetException,
            IllegalAccessException {
        Method doGetMethod = getDoGetMethod();

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        doGetMethod.invoke(dateServletObject, request, response);
        assertThat(stringWriter.getBuffer().toString()).contains(LocalDate.now().toString());
    }

    private Method getDoGetMethod() {
        var method = Arrays.stream(dateServletClass.getDeclaredMethods())
                .filter(m -> m.getName().equals("doGet"))
                .findAny()
                .orElseThrow();
        method.setAccessible(true);
        return method;
    }
}