package com.bobocode;

import com.bobocode.annotation.Trimmed;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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

    @Nullable
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        boolean hasTrimmedParameter = hasTrimmedParameterAnnotation(beanClass.getMethods());
        if (hasTrimmedParameter) {
            ProxyFactory proxyFactory = new ProxyFactory(bean);
            proxyFactory.setProxyTargetClass(true);
            proxyFactory.addAdvice(new TrimmedMethodInterceptor());
            return proxyFactory.getProxy();
        }
        return bean;
    }

    private static class TrimmedMethodInterceptor implements MethodInterceptor {
        @Nullable
        @Override
        public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
            Object[] arguments = invocation.getArguments();
            Parameter[] parameters = invocation.getMethod().getParameters();

            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].isAnnotationPresent(Trimmed.class)
                        && arguments[i] instanceof String str) {
                    arguments[i] = str.trim();
                }
            }

            return invocation.proceed();
        }
    }

    private boolean hasTrimmedParameterAnnotation(Method[] methods) {
        for (Method method : methods) {
            for (Parameter parameter : method.getParameters()) {
                if (parameter.getAnnotation(Trimmed.class) != null && parameter.getType().equals(String.class)) {
                    return true;
                }
            }
        }
        return false;
    }
}
