package com.bobocode;

import com.bobocode.config.RootConfig;
import com.bobocode.config.WebConfig;
import com.bobocode.web.controller.WelcomeController;
import org.junit.jupiter.api.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WebAppConfigurationTest {

    @Test
    @Order(1)
    @DisplayName("RootConfig class is marked as @Configuration")
    void rootConfigClassIsMarkedAsConfiguration() {
        Configuration configuration = RootConfig.class.getAnnotation(Configuration.class);

        assertNotNull(configuration);
    }

    @Test
    @Order(2)
    @DisplayName("RootConfig class enables @ComponentScan")
    void rootConfigClassEnablesComponentScan() {
        ComponentScan componentScan = RootConfig.class.getAnnotation(ComponentScan.class);

        assertNotNull(componentScan);
    }

    @Test
    @Order(3)
    @DisplayName("RootConfig @ComponentScan contains base packages")
    void rootConfigComponentScanPackages() {
        ComponentScan componentScan = RootConfig.class.getAnnotation(ComponentScan.class);
        String[] packages = componentScan.basePackages();
        if (packages.length == 0) {
            packages = componentScan.value();
        }

        assertThat(packages).contains("com.bobocode");
    }

    @Test
    @Order(4)
    @DisplayName("RootConfig @ComponentScan contains web scan filters")
    void rootConfigComponentScanFilters() {
        ComponentScan componentScan = RootConfig.class.getAnnotation(ComponentScan.class);
        Filter[] filters = componentScan.excludeFilters();
        List<Class> filteredClasses = getFilteredClasses(filters);

        assertThat(filters.length).isEqualTo(2);
        assertThat(filters[0].type()).isEqualTo(FilterType.ANNOTATION);
        assertThat(filters[1].type()).isEqualTo(FilterType.ANNOTATION);
        assertThat(filteredClasses.toArray()).containsExactlyInAnyOrder(EnableWebMvc.class, Controller.class);
    }

    @Test
    @Order(5)
    @DisplayName("WebConfig is marked as @Configuration")
    void webConfigIsMarkedAsConfiguration() {
        Configuration configuration = WebConfig.class.getAnnotation(Configuration.class);

        assertNotNull(configuration);
    }

    @Test
    @Order(6)
    @DisplayName("WebConfig enables @ComponentScan")
    void webConfigEnablesComponentScan() {
        ComponentScan componentScan = WebConfig.class.getAnnotation(ComponentScan.class);

        assertNotNull(componentScan);
    }

    @Test
    @Order(7)
    @DisplayName("WebConfig @ComponentScan contains web packages")
    void webConfigComponentScanPackages() {
        ComponentScan componentScan = WebConfig.class.getAnnotation(ComponentScan.class);

        assertThat(componentScan.basePackages()).contains("com.bobocode.web");
    }

    @Test
    @Order(8)
    @DisplayName("WebConfig is marked with @EnableWebMvc")
    void webConfigEnablesWebMvc() {
        EnableWebMvc enableWebMvc = WebConfig.class.getAnnotation(EnableWebMvc.class);

        assertNotNull(enableWebMvc);
    }

    @Test
    @Order(9)
    @DisplayName("Initializer method is overridden with RootConfig class")
    void initializerRootConfigClasses() {
        WebAppInitializerWrapper webAppInitializerWrapper = new WebAppInitializerWrapper();

        assertThat(webAppInitializerWrapper.getRootConfigClasses()).contains(RootConfig.class);
    }

    @Test
    @Order(10)
    @DisplayName("Initializer method is overridden with WebConfig class")
    void initializerWebConfigClasses() {
        WebAppInitializerWrapper webAppInitializerWrapper = new WebAppInitializerWrapper();

        assertThat(webAppInitializerWrapper.getServletConfigClasses()).contains(WebConfig.class);
    }

    @Test
    @Order(11)
    @DisplayName("Dispatcher Servlet mapping is \"/\"")
    void dispatcherServletMapping() {
        WebAppInitializerWrapper webAppInitializerWrapper = new WebAppInitializerWrapper();

        assertThat(webAppInitializerWrapper.getServletMappings()).contains("/");
    }

    @Test
    @Order(12)
    @DisplayName("WelcomeController is marked as @Controller")
    void welcomeControllerIsMarkedAsController() {
        Controller controller = WelcomeController.class.getAnnotation(Controller.class);

        assertNotNull(controller);
    }

    @Test
    @Order(13)
    @DisplayName("WelcomeController method is marked as Get method")
    void welcomeControllerMethodIsMarkedAsGetMethod() throws NoSuchMethodException {
        GetMapping getMapping = WelcomeController.class.getDeclaredMethod("welcome").getAnnotation(GetMapping.class);

        assertNotNull(getMapping);
    }

    @Test
    @Order(14)
    @DisplayName("WelcomeController Get method is marked properly")
    void welcomeControllerMethodMapping() throws NoSuchMethodException {
        GetMapping getMapping = WelcomeController.class
                .getDeclaredMethod("welcome").getAnnotation(GetMapping.class);

        assertThat(getMapping.value()).contains("/welcome");
    }

    @Test
    @Order(15)
    @DisplayName("WelcomeController Get method is marked as @ResponseBody")
    void welcomeControllerMethodIsMarkedAsResponseBody() throws NoSuchMethodException {
        ResponseBody responseBody = WelcomeController.class
                .getDeclaredMethod("welcome").getAnnotation(ResponseBody.class);

        assertNotNull(responseBody);
    }

    private List<Class> getFilteredClasses(Filter[] filters) {
        return Stream.of(filters).flatMap(filter -> Stream.of(filter.value())).collect(Collectors.toList());
    }
}
