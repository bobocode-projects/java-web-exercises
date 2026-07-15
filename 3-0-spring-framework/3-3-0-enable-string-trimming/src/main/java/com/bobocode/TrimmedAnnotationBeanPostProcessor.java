package com.bobocode;

import com.bobocode.annotation.EnableStringTrimming;
import com.bobocode.annotation.Trimmed;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * This is processor class implements {@link BeanPostProcessor}, looks for a beans where method parameters are marked with
 * {@link Trimmed} annotation, creates proxy of them, overrides methods and trims all {@link String} arguments marked with
 * {@link Trimmed}. For example if there is a string " Java   " as an input parameter it has to be automatically trimmed to "Java"
 * if parameter is marked with {@link Trimmed} annotation.
 * <p>
 *
 * Note! This bean is not marked as a {@link Component} to avoid automatic scanning, instead it should be created in
 * {@link StringTrimmingConfiguration} class which can be imported to a {@link Configuration} class by annotation
 * {@link EnableStringTrimming}
 */
public class TrimmedAnnotationBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        if (!hasTrimmedStringParameter(beanClass)) {
            return bean;
        }

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(beanClass);
        enhancer.setCallback((MethodInterceptor) (proxy, method, args, methodProxy) -> {
            Object[] processedArgs = trimAnnotatedStringArguments(beanClass, method, args);
            return method.invoke(bean, processedArgs);
        });
        return enhancer.create();
    }

    private boolean hasTrimmedStringParameter(Class<?> beanClass) {
        for (Method method : beanClass.getMethods()) {
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                if (parameterTypes[i].equals(String.class) && hasTrimmedAnnotation(parameterAnnotations[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    private Object[] trimAnnotatedStringArguments(Class<?> beanClass, Method proxyMethod, Object[] args) throws NoSuchMethodException {
        Method beanMethod = beanClass.getMethod(proxyMethod.getName(), proxyMethod.getParameterTypes());
        Annotation[][] parameterAnnotations = beanMethod.getParameterAnnotations();
        Class<?>[] parameterTypes = beanMethod.getParameterTypes();

        Object[] processedArgs = args.clone();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i].equals(String.class)
                    && processedArgs[i] != null
                    && hasTrimmedAnnotation(parameterAnnotations[i])) {
                processedArgs[i] = ((String) processedArgs[i]).trim();
            }
        }
        return processedArgs;
    }

    private boolean hasTrimmedAnnotation(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(Trimmed.class)) {
                return true;
            }
        }
        return false;
    }
}
