package com.bobocode;

import com.bobocode.annotation.EnableStringTrimming;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class to provide a bean of {@link TrimmedAnnotationBeanPostProcessor}.
 * </p>
 * Note! This class is not a {@link Configuration} class itself, but can be imported to a config class by the
 * {@link EnableStringTrimming} annotation
 */
public class StringTrimmingConfiguration {

    @Bean
    public TrimmedAnnotationBeanPostProcessor trimmedAnnotationBeanPostProcessor() {
        return new TrimmedAnnotationBeanPostProcessor();
    }
}
