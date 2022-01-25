package com.bobocode.util;

import com.bobocode.annotation.Trimmed;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.util.Arrays;

public class TrimmedAnnotationBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(Trimmed.class)) {
            return createTrimmedProxy(beanClass);
        }
        return bean;
    }

    private Object createTrimmedProxy(Class<?> beanClass) {
        var enhancer = new Enhancer();
        enhancer.setSuperclass(beanClass);
        enhancer.setInterfaces(beanClass.getInterfaces());
        MethodInterceptor interceptor = (Object obj, Method method, Object[] args, MethodProxy proxy) -> {
            Object[] arguments = Arrays.stream(args)
                    .filter(arg -> arg instanceof String)
                    .map(arg -> ((String) arg).trim())
                    .toArray();

            if (method.getReturnType().equals(String.class)) {
                String invokeSuper = (String) proxy.invokeSuper(obj, arguments);
                return (String) invokeSuper.trim();
            }
            return proxy.invokeSuper(obj, arguments);
        };
        enhancer.setCallback(interceptor);
        return beanClass.cast(enhancer.create());
    }
}
