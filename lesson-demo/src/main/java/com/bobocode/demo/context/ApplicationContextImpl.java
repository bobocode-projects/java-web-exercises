package com.bobocode.demo.context;

import com.bobocode.demo.annotation.Component;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContextImpl implements ApplicationContext {
    private Map<String, Object> beanMap = new HashMap<>();
    
    public ApplicationContextImpl(String basePackage) {
        var reflections = new Reflections(basePackage);
        var beanClasses = reflections.getTypesAnnotatedWith(Component.class);
        beanClasses.forEach(System.out::println);
        // todo: 1. Create all bean objects
        // todo: 2. Add them to map by name
        // todo: 3. Inject bean dependencies
    }

    @Override
    public <T> T getBean(Class<T> type) {
        // todo: find a bean by type
        return null;
    }

    @Override
    public <T> T getBean(Class<T> type, String name) {
        // todo: find a bean name and type
        return null;
    }

    @Override
    public <T> T getAllBeans(Class<T> type) {
        // todo: find all bean object of this type
        return null;
    }
}
