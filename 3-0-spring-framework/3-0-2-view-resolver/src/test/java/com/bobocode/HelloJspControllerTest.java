package com.bobocode;

import com.bobocode.config.ResolverConfig;
import com.bobocode.controller.HelloJspController;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.mockito.internal.configuration.ClassPathLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = ResolverConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HelloJspControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final HelloJspController helloJspController = new HelloJspController();

    @Test
    @Order(1)
    @DisplayName("Class marked as @Controller")
    void helloJspControllerClassMarkedAsController() {
        Controller controller = HelloJspController.class.getAnnotation(Controller.class);
        assertNotNull(controller);
    }

    @Test
    @Order(2)
    @DisplayName("Class marked as @RequestMapping")
    void helloJspControllerClassMarkedAsRequestMapping() {
        RequestMapping requestMapping = HelloJspController.class.getAnnotation(RequestMapping.class);
        assertNotNull(requestMapping);
    }

    @Test
    @Order(3)
    @DisplayName("Method that get hello page is marker with @GetMapping")
    void helloJspControllerHasMethodAnnotatedAsGetMapping() {
        boolean hasAnnotation = Arrays.stream(HelloJspController.class.getMethods())
                .anyMatch(method ->
                        method.getAnnotation(GetMapping.class) != null
                );
        assertTrue(hasAnnotation);
    }

    @Test
    @Order(4)
    @SneakyThrows
    @DisplayName("Get method returns view name \"hello\"")
    void helloJspControllerMethodReturnsHelloViewName() {
        Method method = Arrays.stream(HelloJspController.class.getMethods())
                .filter(m -> m.getAnnotation(GetMapping.class) != null)
                .findFirst()
                .orElseThrow();

        var viewName = method.invoke(helloJspController);

        assertEquals("hello", viewName);
    }

    @Test
    @Order(5)
    @SneakyThrows
    @DisplayName("GET method is completed ✅")
    void getRequestShouldReturnStatusOkAndCorrectData() {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("hello"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("/WEB-INF/views/hello.jsp"));
    }
}
