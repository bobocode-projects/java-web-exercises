package com.bobocode.demo.context;

import com.bobocode.demo.annotation.Autowired;
import com.bobocode.demo.annotation.Component;
import com.bobocode.demo.annotation.Trimmed;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

public class ApplicationContextImpl implements ApplicationContext { 
    private final Map<String, Object> beanMap = new HashMap<>();

    public ApplicationContextImpl(String basePackage) {
        var componentClasses = scanPackageForComponents(basePackage);
        createBeans(componentClasses);
        postProcessBeans();
    }

    @Override
    public <T> T getBean(Class<T> type) {
        var beans = getAllBeans(type);
        if (beans.size() > 1) {
            throw new RuntimeException("No unique bean exception");
        }
        return beans.values().stream().findAny().orElseThrow();
    }

    @Override
    public <T> T getBean(Class<T> type, String name) {
        var bean = beanMap.get(name);
        return type.cast(bean);
    }

    @Override
    public <T> Map<String, T> getAllBeans(Class<T> type) {
        return beanMap.entrySet()
                .stream()
                .filter(entry -> type.isAssignableFrom(entry.getValue().getClass()))
                .collect(toMap(Map.Entry::getKey, e -> type.cast(e.getValue())));
    }

    private Set<Class<?>> scanPackageForComponents(String basePackage) {
        var reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Component.class);
    }

    @SneakyThrows
    private void createBeans(Set<Class<?>> beanClasses) {
        for (var beanType : beanClasses) {
            var constructor = beanType.getConstructor();
            var bean = constructor.newInstance();
            var beanName = resolveBeanName(beanType);
            beanMap.put(beanName, bean);
        }
    }

    private String resolveBeanName(Class<?> beanType) {
        var explicitName = beanType.getAnnotation(Component.class).value();
        return explicitName.isEmpty() ? beanType.getSimpleName() : explicitName;
    }

    private void postProcessBeans() {
        trimmingPostProcessing();
        autowiringPostProcessing();
    }

    @SneakyThrows
    private void autowiringPostProcessing() {
        for (var beanEntry : beanMap.entrySet()) {
            var bean = beanEntry.getValue();
            var beanType = bean.getClass();
            for (var field : beanType.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    var dependency = getBean(field.getType());
                    field.setAccessible(true);
                    field.set(bean, dependency);
                }
            }
        }
    }

    private void trimmingPostProcessing() {
        for (var beanEntry : beanMap.entrySet()) {
            var beanName = beanEntry.getKey();
            var bean = beanEntry.getValue();
            if (requiresProxying(beanEntry.getValue())) {
                var beanProxy = createProxy(bean);
                beanMap.put(beanName, beanProxy);
            }
        }
    }

    private boolean requiresProxying(Object value) {
        for (var method : value.getClass().getDeclaredMethods()) {
            for (var param : method.getParameters()) {
                if (param.isAnnotationPresent(Trimmed.class)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Object createProxy(Object bean) {
        var beanType = bean.getClass();
        var enhancer = new Enhancer();
        enhancer.setSuperclass(beanType);
        enhancer.setInterfaces(beanType.getInterfaces());
        MethodInterceptor trimmingMethodInterceptor = (obj, method, args, methodProxy) -> {
            var params = method.getParameters();
            for (int i = 0; i < params.length; i++) {
                var param = params[i];
                if (param.isAnnotationPresent(Trimmed.class)) {
                    if (param.getType().equals(String.class)) {
                        args[i] = ((String) args[i]).trim();
                    } else {
                        throw new RuntimeException("Only String params can be trimmed");
                    }
                }
            }
            return methodProxy.invokeSuper(obj, args);
        };
        enhancer.setCallback(trimmingMethodInterceptor);
        return enhancer.create();
    }
}
