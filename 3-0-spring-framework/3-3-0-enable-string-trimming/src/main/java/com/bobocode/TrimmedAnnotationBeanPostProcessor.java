package com.bobocode;

import com.bobocode.annotation.Trimmed;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * This is processor class implements {@link BeanPostProcessor}, looks for a beans where method parameters are marked with
 * {@link Trimmed} annotation, creates proxy of them, overrides methods and trims all {@link String} arguments marked with
 * {@link Trimmed}. For example if there is a string " Java   " as an input parameter it has to be automatically trimmed to "Java"
 * if parameter is marked with {@link Trimmed} annotation.
 * <p>
 * <p>
 * Note! This bean is not marked as a {@link Component} to avoid automatic scanning, instead it should be created in
 * {@link StringTrimmingConfiguration} class which can be imported to a {@link Configuration} class by annotation
 * {@link EnableStringTrimming}
 */
public class TrimmedAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final Map<String, Object> beansToBeProxied = new HashMap<>();
    private final Map<String, MethodInterceptor[]> beanInterceptors = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (containsParametersAnnotatedWithTrimmed(bean)) {
            beansToBeProxied.put(beanName, bean);
            beanInterceptors.put(beanName, new MethodInterceptor[]{provideTrimmingInterceptor()});
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Object toProxy = beansToBeProxied.get(beanName);
        if (toProxy != null) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(toProxy.getClass());
            setInterceptorsIfNeeded(enhancer, bean, beanName);
            return enhancer.create();
        }
        return bean;
    }

    private MethodInterceptor provideTrimmingInterceptor() {
        return (beanInstance, method, args, methodProxy) ->
                methodProxy.invokeSuper(beanInstance, processParameters(method, args));
    }

    private Object[] processParameters(Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(Trimmed.class)) {
                args[i] = trimMarkedString(args[i]);
            }
        }
        return args;
    }

    private Object trimMarkedString(Object arg) {
        if (arg instanceof String string) {
            return StringUtils.hasLength(string) ? string.trim() : arg;
        }
        return arg;
    }

    private boolean containsParametersAnnotatedWithTrimmed(Object bean) {
        return Arrays.stream(bean.getClass().getDeclaredMethods()).flatMap(m -> Stream.of(m.getParameters()))
                .anyMatch(p -> p.isAnnotationPresent(Trimmed.class));
    }

    private void setInterceptorsIfNeeded(Enhancer enhancer, Object bean, String beanName) {
        MethodInterceptor[] methodInterceptors = beanInterceptors.get(beanName);
        if (methodInterceptors.length != 0) {
            Callback[] interceptors = findInterceptors(bean);
            if (interceptors.length != 0) {
                MethodInterceptor[] newInterceptors = Arrays.copyOf(methodInterceptors, methodInterceptors.length + 1);
                newInterceptors[newInterceptors.length - 1] = provideTrimmingInterceptor();
                enhancer.setCallbacks(newInterceptors);
            }
            enhancer.setCallbacks(methodInterceptors);
        }
    }

    @SneakyThrows
    private MethodInterceptor[] findInterceptors(Object bean) {
        var method = Arrays.stream(bean.getClass().getDeclaredMethods()).filter(m -> m.getName().equals("getCallbacks"))
                .findFirst();
        if (method.isPresent()) {
            return (MethodInterceptor[]) method.get().invoke(bean);
        }
        return new MethodInterceptor[0];
    }
}
