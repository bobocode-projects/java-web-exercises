package com.bobocode.util;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StringTrimmingConfiguration {

    @Bean
    public BeanPostProcessor trimmedAnnotationBeanPostProcessor(){
        return new TrimmedAnnotationBeanPostProcessor();
    }
}
