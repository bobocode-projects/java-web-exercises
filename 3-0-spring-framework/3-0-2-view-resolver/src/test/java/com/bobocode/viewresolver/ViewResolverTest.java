package com.bobocode.viewresolver;

import com.bobocode.viewresolver.config.ResolverConfig;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest
@ContextConfiguration(classes = {ResolverConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ViewResolverTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    @DisplayName("Class is marked as @Configuration")
    void classIsMarkedAsConfiguration() {
        Configuration configuration = ResolverConfig.class.getAnnotation(Configuration.class);
        assertNotNull(configuration);
    }

    @Test
    @Order(2)
    @DisplayName("Configuration Class has annotation @EnableWebMvc")
    void classAnnotatedWithEnableWebMvc() {
        EnableWebMvc enableWebMvc = ResolverConfig.class.getAnnotation(EnableWebMvc.class);
        assertNotNull(enableWebMvc);
    }

    @Test
    @Order(3)
    @DisplayName("Configuration Class has annotation @ComponentScan")
    void classAnnotatedWithComponentScan() {
        ComponentScan componentScan = ResolverConfig.class.getAnnotation(ComponentScan.class);
        assertNotNull(componentScan);
    }

    @Test
    @Order(4)
    @DisplayName("ComponentScan packages are indicated")
    void ComponentScanHasIndicatedPackages() {
        ComponentScan componentScan = ResolverConfig.class.getAnnotation(ComponentScan.class);
        assertEquals("com.bobocode.resolver", componentScan.basePackages()[0]);
    }

    @Test
    @Order(5)
    @DisplayName("Config class implements WebMvcConfigurer interface")
    void isConfigClassImplementsWebMvcConfigurerInterface() {
        assertTrue(WebMvcConfigurer.class.isAssignableFrom(ResolverConfig.class));
    }

    @SneakyThrows
    @Test
    @Order(6)
    @DisplayName("Config class overrides configureViewResolvers method")
    void isConfigurationClassContainsViewResolverMethod() {
        Method configureViewResolvers = ResolverConfig.class.getMethod("configureViewResolvers", ViewResolverRegistry.class);
        assertNotNull(configureViewResolvers);
    }

    @Test
    @Order(7)
    @SneakyThrows
    @DisplayName("Get request returns correct view name and URL")
    void getRequestReturnCorrectUrlAndViewName() {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("/WEB-INF/views/index.jsp"));
    }
}
