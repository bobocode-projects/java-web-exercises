package com.bobocode.config;

import com.bobocode.TestDataGenerator;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

/**
 * This class specify application context configuration. Basically, it's all about which instances of which classes
 * should be created and registered in the context. An instance that is registered in the context is called 'bean'.
 * <p>
 * To tell the container, which bean should be created, you could either specify packages to scan using @{@link ComponentScan},
 * or declare your own beans using @{@link Bean}. When you use @{@link ComponentScan} the container will discover
 * specified package and its sub-folders, to find all classes marked @{@link Component}.
 * <p>
 * If you want to import other configs from Java class or XML file, you could use @{@link Import}
 * and @{@link ImportResource} accordingly
 * <p>
 * todo 1: make this class a Spring configuration class
 * todo 2: enable component scanning for dao and service packages
 * todo 3: provide explicit configuration for a bean of type {@link TestDataGenerator} with name "dataGenerator" in this class.
 * hint: use method creation approach with @Bean annotation;
 * todo 4: Don't specify bean name "dataGenerator" explicitly
 */

@Configuration
@ComponentScan(basePackages = {"com.bobocode.dao", "com.bobocode.service"})
public class ApplicationConfig {

    @Bean
    public TestDataGenerator dataGenerator() {
        return new TestDataGenerator();
    }
}
