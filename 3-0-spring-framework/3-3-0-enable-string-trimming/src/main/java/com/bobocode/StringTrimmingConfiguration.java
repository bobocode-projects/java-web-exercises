package com.bobocode;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class StringTrimmingConfiguration {

    @Bean
    public TrimmedAnnotationBeanPostProcessor postProcessorBean(){
        return new TrimmedAnnotationBeanPostProcessor();
    }
}
