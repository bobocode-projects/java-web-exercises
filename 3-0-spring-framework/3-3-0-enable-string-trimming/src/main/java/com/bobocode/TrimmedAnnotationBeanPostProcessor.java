package com.bobocode;

import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * This is processor class implements {@link BeanPostProcessor}, looks for a beans where method parameters marked with
 * {@link Trimmed} annotation, creates proxy of them, overrides methods and trims all {@link String} arguments marked with
 * {@link Trimmed}. For example if there is a string " Java   " as an input parameter it has to be
 * automatically trimmed to "Java" if parameter market with {@link Trimmed} annotation.
 */
public class TrimmedAnnotationBeanPostProcessor {
//todo: Implement TrimmedAnnotationBeanPostProcessor according to javadoc
//todo: Extract creating of TrimmedAnnotationBeanPostProcessor bean to StringTrimmingConfiguration config class
//todo: Implement @EnableStringTrimming annotation to import StringTrimmingConfiguration config to another configuration classes
}
