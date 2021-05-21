package com.bobocode;

import com.bobocode.config.RootConfig;
import com.bobocode.config.WebConfig;
import com.bobocode.dao.impl.InMemoryAccountDao;
import org.junit.jupiter.api.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
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
    @DisplayName("RootConfig class is configured properly")
    void rootConfigClassIsConfigured() {
        Configuration configuration = RootConfig.class.getAnnotation(Configuration.class);
        ComponentScan componentScan = RootConfig.class.getAnnotation(ComponentScan.class);
        String[] packages = componentScan.basePackages();
        if (packages.length == 0) {
            packages = componentScan.value();
        }

        assertNotNull(configuration);
        assertNotNull(componentScan);
        assertThat(packages).contains("com.bobocode");

        Filter[] filters = componentScan.excludeFilters();
        List<Class> filteredClasses = getFilteredClasses(filters);

        assertThat(filters.length).isEqualTo(2);
        assertThat(filters[0].type()).isEqualTo(FilterType.ANNOTATION);
        assertThat(filters[1].type()).isEqualTo(FilterType.ANNOTATION);
        assertThat(filteredClasses.toArray()).containsExactlyInAnyOrder(EnableWebMvc.class, Controller.class);
    }

    @Test
    @Order(2)
    @DisplayName("WebConfig class is configured properly")
    void webConfigClassIsConfiguredProperly() {
        Configuration configuration = WebConfig.class.getAnnotation(Configuration.class);
        ComponentScan componentScan = WebConfig.class.getAnnotation(ComponentScan.class);
        EnableWebMvc enableWebMvc = WebConfig.class.getAnnotation(EnableWebMvc.class);

        assertNotNull(configuration);
        assertNotNull(componentScan);
        assertThat(componentScan.basePackages()).contains("com.bobocode.web");
        assertNotNull(enableWebMvc);
    }

    private List<Class> getFilteredClasses(Filter[] filters) {
        return Stream.of(filters).flatMap(filter -> Stream.of(filter.value())).collect(Collectors.toList());
    }

    @Test
    @Order(3)
    @DisplayName("InMemoryAccountDao class is configured properly")
    void inMemoryAccountDaoClassIsConfiguredProperly() {
        Component component = InMemoryAccountDao.class.getAnnotation(Component.class);

        assertNotNull(component);
        assertThat(component.value()).contains("accountDao");
    }
}
